package org.quickstart.jetty.openid4java.client;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;

public class ClientServlet extends HttpServlet {

  //private static String myopenid = "http://localhost:8081/server/login";
  private static String myopenid = "http://yangziliang.openid.org.cn/";
  private static String returnToUrl = "http://localhost:8080/client/hehe";

  private static ConsumerManager consumerManager;
  private static DiscoveryInformation discoveryInformation;
  private static AtomicInteger count = new AtomicInteger();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {

      if (request.getPathInfo().contains("hehe")) {

        System.out.println(request.getPathInfo());
        System.out.println(request.getParameterMap());

        ParameterList parameterList = new ParameterList(request.getParameterMap());

        VerificationResult verificationResult = consumerManager
            .verify(returnToUrl, parameterList,
                discoveryInformation);

        Identifier verifiedIdentifier = verificationResult.getVerifiedId();
        if (verifiedIdentifier != null) {

          AuthSuccess authSuccess = (AuthSuccess) verificationResult.getAuthResponse();

          if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
            MessageExtension extension = authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);
            if (extension instanceof SRegResponse) {

              SRegResponse sRegResponse = (SRegResponse) extension;

              System.out.println("openid=" + verifiedIdentifier.getIdentifier());
              System.out.println("bob=" + sRegResponse.getAttributeValue("dob"));
              System.out.println("email=" + sRegResponse.getAttributeValue("email"));
              System.out.println("fullname=" + sRegResponse.getAttributeValue("fullname"));
              System.out.println("postcode=" + sRegResponse.getAttributeValue("postcode"));
            }

          }

        }
      } else {

        consumerManager = new ConsumerManager();
        consumerManager.setAssociations(new InMemoryConsumerAssociationStore());
        consumerManager.setNonceVerifier(new InMemoryNonceVerifier(10000));

        //这里是向openidProvider请求discover信息
        List discoveries = consumerManager.discover(myopenid);
        // Pass the discoveries to the associate() method...
        discoveryInformation = consumerManager.associate(discoveries);

        // Create the AuthRequest object
        AuthRequest authRequest = consumerManager.authenticate(discoveryInformation, returnToUrl);
        // Create the Simple Registration Request
        SRegRequest sRegRequest = SRegRequest.createFetchRequest();
        sRegRequest.addAttribute("email", false);
        sRegRequest.addAttribute("fullname", false);
        sRegRequest.addAttribute("dob", false);
        sRegRequest.addAttribute("postcode", false);
        authRequest.addExtension(sRegRequest);

        // Now take the AuthRequest and forward it on to the OP
        response.sendRedirect(authRequest.getDestinationUrl(true));
      }

    } catch (
        Exception ex) {
      ex.printStackTrace();
    }


  }


}
