package org.quickstart.container.jersey.http;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.quickstart.container.jersey.http.HelloWorldResource;

public class MyResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(HelloWorldResource.class);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        final String responseMsg = target().path("helloworld").request().get(String.class);

        assertEquals(HelloWorldResource.CLICHED_MESSAGE, responseMsg);
    }
}
