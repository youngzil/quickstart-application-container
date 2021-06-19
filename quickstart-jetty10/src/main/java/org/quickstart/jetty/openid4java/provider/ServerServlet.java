package org.quickstart.jetty.openid4java.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.Message;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.openid4java.server.InMemoryServerAssociationStore;
import org.openid4java.server.ServerManager;

public class ServerServlet extends HttpServlet {


  private static String opEndpointUrl = "http://localhost:8081/server/login";

  private static DiscoveryInformation discoveryInformation;
  private static ServerManager serverManager;

  private static final String correctUserId = "steve";
  private static final String correctPassword = "asdf";


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {

      Map<String, String[]> parameterMap = request.getParameterMap();

      if (request.getPathInfo().contains("login")) {
        //如果是登陆，就校验并且返回
        //String userId = ((String[]) parameterMap.get("userId"))[0];
        //String password = ((String[]) parameterMap.get("password"))[0];

        //boolean authenticatedAndApproved =
        //    correctUserId.equals(userId) && correctPassword.equals(password);
        if (true) {
          ParameterList requestParameters = new ParameterList(parameterMap);
          // Otherwise, create auth response and redirect

          sendSuccessfulResponse(response, requestParameters);

        } else {
          System.out.println("Login failed!");
        }

      } else {
        //是第一次过来，判断是否已经登录了，已经登陆了直接返回结果，否则返回登录页面
        if (parameterMap.isEmpty()) {
          // Empty request. Assume discovery request...
          //log.debug("Processing empty request. Assuming discovery request...");
          sendDiscoveryResponse(response);
        } else {
          ParameterList requestParameters = new ParameterList(parameterMap);
          String mode = requestParameters.hasParameter("openid.mode") ? requestParameters
              .getParameterValue("openid.mode") : null;
          //log.info("Processing OpenID request '" + mode + "'...");
          // Save off the return_to value so when the user logs in successfully,
          /// we can redirect the browser there...
          // Crack the Request mode and process it accordingly...
          if ("associate".equals(mode)) {
            processAssociationRequest(response, requestParameters);
          } else if ("checkid_immediate".equals(mode)
              ||
              "checkid_setup".equals(mode)
              ||
              "check_authentication".equals(mode)) {
            // Check Session. If information is there, we're done. No need to login again.
            boolean hashLogin = true;
            if (hashLogin) {
              // Create AuthResponse from session variables...
              //log.info("********************************");
              //log.info("* User is already logged in... *");
              //log.info("********************************");
              sendSuccessfulResponse(response, requestParameters);
            }
          } else {
            //log.error("Unknown request mode '" + mode + "'... Forcing login...");
          }
        }
      }

    } catch (
        Exception ex) {
      ex.printStackTrace();
    }

  }


  private void sendSuccessfulResponse(HttpServletResponse response, ParameterList requestParameters)
      throws IOException {
    //log.trace("sendSuccessfulResponse() BEGIN...");

    String userSelectedId = opEndpointUrl;
    String userSelectedClaimedId = opEndpointUrl;

    // Create the AuthResponse
    Message authResponse = buildAuthResponse(
        requestParameters,
        userSelectedId,
        userSelectedClaimedId);
    response.sendRedirect(authResponse.getDestinationUrl(true));
    //log.trace("sendSuccessfulResponse() END...");

  }


  private void processAssociationRequest(HttpServletResponse response, ParameterList request)
      throws IOException {
    //log.trace("processAssociationRequest() BEGIN...");
    Message message = getServerManager().associationResponse(request);
    sendPlainTextResponse(response, message);
    //log.trace("processAssociationRequest() END...");
  }

  private static void sendPlainTextResponse(HttpServletResponse response, Message message)
      throws IOException {
    //log.trace("sendPlainTextResponse() BEGIN...");
    response.setContentType("text/plain");
    OutputStream os = response.getOutputStream();
    os.write(message.keyValueFormEncoding().getBytes());
    os.close();
    //log.trace("sendPlainTextResponse() END...");
  }


  private void sendDiscoveryResponse(HttpServletResponse response) throws IOException {
    //log.trace("sendDiscoveryResponse() BEGIN...");
    //
    response.setContentType("application/xrds+xml");
    OutputStream outputStream = response.getOutputStream();
    String xrdsResponse = createXrdsResponse();
    //
    /*if (log.isDebugEnabled()) {
      log.debug("Sending XRDS response:");
      log.debug(xrdsResponse);
    }*/
    outputStream.write(xrdsResponse.getBytes());
    outputStream.close();
    //log.trace("sendDiscoveryResponse() END...");
  }


  public String createXrdsResponse() {
    //log.trace("createXrdsResponse() BEGIN...");
    XrdsDocumentBuilder documentBuilder = new XrdsDocumentBuilder();
    documentBuilder
        .addServiceElement("http://specs.openid.net/auth/2.0/server", opEndpointUrl, "10");
    documentBuilder
        .addServiceElement("http://specs.openid.net/auth/2.0/signon", opEndpointUrl, "20");
    documentBuilder.addServiceElement(AxMessage.OPENID_NS_AX, opEndpointUrl, "30");
    documentBuilder.addServiceElement(SRegMessage.OPENID_NS_SREG, opEndpointUrl, "40");
    //log.trace("createXrdsResponse() BEGIN...");
    return documentBuilder.toXmlString();
  }


  private ServerManager getServerManager() {
    //log.trace("geterverManager() BEGIN...");
    if (serverManager == null) {
      serverManager = new ServerManager();
      serverManager.setOPEndpointUrl(opEndpointUrl);
      serverManager.setPrivateAssociations(new InMemoryServerAssociationStore());
      serverManager.setSharedAssociations(new InMemoryServerAssociationStore());
    }
    //log.debug("Returning ServerManager =>" + serverManager.toString() + "<=");
    //log.trace("geterverManager() END...");
    return serverManager;
  }


  private Message buildAuthResponse(ParameterList requestParameters, String userSelectedId,
      String userSelectedClaimedId) {
    //log.trace("buildAuthResponse() BEGIN...");
    Message authResponse = createAuthResponse(
        requestParameters,
        userSelectedId,
        userSelectedClaimedId,
        true // authenticated... yep
    );
    // Check for and process any extensions the RP has asked for
    AuthRequest authRequest = null;
    try {
      authRequest = AuthRequest
          .createAuthRequest(requestParameters, getServerManager().getRealmVerifier());
      // Process all of the extensions we care about...
      // Simple Registration
      if (authRequest.hasExtension(SRegMessage.OPENID_NS_SREG)) {
        //log.debug("Processing Simple Registration Extension request...");
        MessageExtension extensionRequestObject = authRequest
            .getExtension(SRegMessage.OPENID_NS_SREG);
        if (extensionRequestObject instanceof SRegRequest) {
          SRegRequest sRegRequest = (SRegRequest) extensionRequestObject;
          // Send back everything we have (which you'll notice is not everything)
          /// required or not. We're friendly...
          Map<String, String> registrationData = new HashMap<String, String>();
          registrationData.put("email", "steve@makotoconsulting.com");
          registrationData.put("fullname", "James Steven Perry");
          registrationData.put("dob", Long.toString(new Date().getTime()));
          registrationData.put("postcode", "72207");

          SRegResponse sRegResponse = SRegResponse
              .createSRegResponse(sRegRequest, registrationData);
          // Add the information to the AuthResponse message
          authResponse.addExtension(sRegResponse);
        } else {
          //log.error("Cannot continue processing Simple Registration Extension. The object returned from the AuthRequest (of type " + extensionRequestObject.getClass().getName() + ") claims to be correct, but is not of type " + SRegRequest.class.getName() + " as expected.");
        }
      }
      if (authRequest.hasExtension(AxMessage.OPENID_NS_AX)) {
        //log.debug("Processing Attribute Exchange request...");
        MessageExtension extensionRequestObject = authRequest.getExtension(AxMessage.OPENID_NS_AX);
        FetchResponse fetchResponse = null;
        Map<String, String> axData = new HashMap<String, String>();
        if (extensionRequestObject instanceof FetchRequest) {
          FetchRequest axRequest = (FetchRequest) extensionRequestObject;
          ParameterList parameters = axRequest.getParameters();
          fetchResponse = FetchResponse.createFetchResponse(axRequest, axData);
          if (parameters.hasParameter("type.favoriteColor")) {
            axData.put("favoriteColor", "blue");
            fetchResponse
                .addAttribute("favoriteColor", "http://makotogroup.com/schema/1.0/favoriteColor",
                    "blue");
          }
          authResponse.addExtension(fetchResponse);
        } else {
          //log.error("Cannot continue processing Attribute Exchange (AX) request. The object returned from the AuthRequest (of type " + extensionRequestObject.getClass().getName() + ") claims to be correct, but is not of type " + AxMessage.class.getName() + " as expected.");
        }
      }
      getServerManager().sign((AuthSuccess) authResponse);
      // TODO: Add Custom extension where we send back Favorite Color
    } catch (Exception e) {
      //log.error("Error occurred creating AuthRequest object:", e);
    }
    //log.trace("buildAuthResponse() End...");
    return authResponse;
  }


  private Message createAuthResponse(ParameterList requestParameters, String userSelectedId,
      String userSelectedClaimedId,
      boolean authenticatedAndApproved) {
    //log.trace("createAuthResponse() BEGIN...");
    //log.debug("Creating Auth Response. UserSelectedId =>" + userSelectedId + "<= userSelectedClaimedId =>" + userSelectedClaimedId + "<=");
    Message authResponse = getServerManager()
        .authResponse(requestParameters, userSelectedId, userSelectedClaimedId,
            authenticatedAndApproved);
    //log.trace("createAuthResponse() END...");
    return authResponse;
  }

}
