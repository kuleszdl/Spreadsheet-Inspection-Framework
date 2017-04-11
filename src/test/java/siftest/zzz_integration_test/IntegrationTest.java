package siftest.zzz_integration_test;

import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.*;
import org.junit.rules.ExpectedException;
import sif.api.HeartbeatResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@SuppressWarnings("unused")
public class IntegrationTest {

    private static final String POLICIES_PATH = "src/test/resources/policies/";
    private static final String SPREADSHEETS_PATH = "src/test/resources/spreadsheets/";
    private static final String SAMPLES_PATH = "src/test/resources/samples/";
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
    private static final String SIMPLE_SPREADSHEET_RESPONSE_HASH = "";
    private static EmbeddedJetty embeddedJetty;
    private static Client client;
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.JavaUtilLog");
        System.setProperty("org.eclipse.jetty.util.log.class.LEVEL", "ERROR");
        embeddedJetty = new EmbeddedJetty();
        embeddedJetty.start();
        client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        embeddedJetty.stop();
    }

    private MultiPart createMultiPartData(File policyFile, File spreadsheetFile) throws IOException {
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
        FileDataBodyPart policy = new FileDataBodyPart("policy", policyFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        FileDataBodyPart spreadsheet = new FileDataBodyPart("spreadsheet", spreadsheetFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.bodyPart(policy);
        multiPart.bodyPart(spreadsheet);
        multiPart.close();
        return multiPart;
    }

    private static boolean isEqualInputStream(InputStream i1, InputStream i2) throws IOException {

        ReadableByteChannel ch1 = Channels.newChannel(i1);
        ReadableByteChannel ch2 = Channels.newChannel(i2);
        ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
        ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);

        try {
            while (true) {
                int n1 = ch1.read(buf1);
                int n2 = ch2.read(buf2);
                if (n1 == -1 || n2 == -1) return n1 == n2;

                buf1.flip();
                buf2.flip();

                for (int i = 0; i < Math.min(n1, n2); i++)
                    if (buf1.get() != buf2.get())
                        return false;

                buf1.compact();
                buf2.compact();
            }

        } finally {
            if (i1 != null) i1.close();
            if (i2 != null) i2.close();
        }
    }

    @Test
    public void testHeartbeat() {
        System.out.println(embeddedJetty.getBaseUri());
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("heartbeat");
        String entity = webTarget.request(MediaType.TEXT_PLAIN_TYPE).get(String.class);
        Assert.assertEquals(HeartbeatResource.MSG, entity);
    }

    @Test
    public void noPolicy() throws IOException {
        File spreadsheetFile = new File(SPREADSHEETS_PATH + "simple-spreadsheet.xls");
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
        FileDataBodyPart spreadsheet = new FileDataBodyPart("spreadsheet", spreadsheetFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.bodyPart(spreadsheet);
        multiPart.close();
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ooxml");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
        String expectedString = "<inspectionResponse>\n    <errors>\n        <error>NoPolicyException</error>\n    </errors>\n</inspectionResponse>\n";
        Assert.assertEquals(XML_HEADER + expectedString, response.readEntity(String.class));
    }

    @Test
    public void noSpreadsheet() throws IOException {
        File policyFile = new File(POLICIES_PATH + "simple.xml");
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
        FileDataBodyPart policy = new FileDataBodyPart("policy", policyFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.bodyPart(policy);
        multiPart.close();
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ooxml");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
        String expectedString = "<inspectionResponse>\n    <errors>\n        <error>NoSpreadsheetException</error>\n    </errors>\n</inspectionResponse>\n";
        Assert.assertEquals(XML_HEADER + expectedString, response.readEntity(String.class));
    }

    public void testSimpleSpreadsheetXls() throws IOException {
        File policyFile = new File(POLICIES_PATH + "all-in-one.xml");
        File spreadsheetFile = new File(SPREADSHEETS_PATH + "simple-spreadsheet.xls");
        MultiPart multiPart = this.createMultiPartData(policyFile, spreadsheetFile);
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ooxml");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
    }

    public void testSimpleSpreadsheetXlsx() throws IOException {
        File policyFile = new File(POLICIES_PATH + "all-in-one.xml");
        File spreadsheetFile = new File(SPREADSHEETS_PATH + "simple-spreadsheet.xlsx");
        MultiPart multiPart = this.createMultiPartData(policyFile, spreadsheetFile);
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ooxml");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
    }

    @Test
    public void testAllInOneXls() throws IOException {
        File policyFile = new File(POLICIES_PATH + "all-in-one.xml");
        File spreadsheetFile = new File(SPREADSHEETS_PATH + "all-in-one.xls");
        MultiPart multiPart = this.createMultiPartData(policyFile, spreadsheetFile);
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ooxml");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
        InputStream a = response.readEntity(InputStream.class);
        InputStream b = new FileInputStream(SAMPLES_PATH + "all-in-one-xls.xml");
        Assert.assertTrue(isEqualInputStream(a, b));
    }

    @Test
    public void testAllInOneXlsx() throws IOException {
        File policyFile = new File(POLICIES_PATH + "all-in-one.xml");
        File spreadsheetFile = new File(SPREADSHEETS_PATH + "all-in-one.xlsx");
        MultiPart multiPart = this.createMultiPartData(policyFile, spreadsheetFile);
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ooxml");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
        InputStream a = response.readEntity(InputStream.class);
        InputStream b = new FileInputStream(SAMPLES_PATH + "all-in-one-xlsx.xml");
        Assert.assertTrue(isEqualInputStream(a, b));
    }

    @Test
    public void testSimpleSpreadsheetOds() throws IOException {
        File policyFile = new File(POLICIES_PATH + "simple.xml");
        File spreadsheetFile = new File(SPREADSHEETS_PATH + "simple-spreadsheet.ods");
        MultiPart multiPart = this.createMultiPartData(policyFile, spreadsheetFile);
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ods");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
        String expectedString = "<inspectionResponse/>\n";
        Assert.assertEquals(XML_HEADER + expectedString, response.readEntity(String.class));
    }

    @Test
    public void testBadPolicy() throws IOException {
        File policyFile = new File(POLICIES_PATH + "malformed.xml");
        File spreadsheetFile = new File(SPREADSHEETS_PATH + "simple-spreadsheet.xls");
        MultiPart multiPart = createMultiPartData(policyFile, spreadsheetFile);
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ooxml");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
        String expectedString = "<inspectionResponse>\n    <errors>\n        <error>InvalidPolicyException</error>\n    </errors>\n</inspectionResponse>\n";
        Assert.assertEquals(XML_HEADER + expectedString, response.readEntity(String.class));
    }

    @Test
    public void testBadXlsSpreadsheet() throws IOException {
        File policyFile = new File(POLICIES_PATH + "simple.xml");
        File spreadsheetFile = new File(SPREADSHEETS_PATH + "simple-spreadsheet-bad.xls");
        MultiPart multiPart = createMultiPartData(policyFile, spreadsheetFile);
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ooxml");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
        String expectedString = "<inspectionResponse>\n    <errors>\n        <error>InvalidSpreadsheetException</error>\n    </errors>\n</inspectionResponse>\n";
        Assert.assertEquals(XML_HEADER + expectedString, response.readEntity(String.class));
    }

    @Test
    public void testBadXlsxSpreadsheet() throws IOException {
        File policyFile = new File(POLICIES_PATH + "simple.xml");
        File spreadsheetFile = new File(SPREADSHEETS_PATH + "simple-spreadsheet-bad.xlsx");
        MultiPart multiPart = createMultiPartData(policyFile, spreadsheetFile);
        WebTarget webTarget = client.target(embeddedJetty.getBaseUri()).path("ooxml");
        Response response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(multiPart, multiPart.getMediaType()));
        String expectedString = "<inspectionResponse>\n    <errors>\n        <error>InvalidSpreadsheetException</error>\n    </errors>\n</inspectionResponse>\n";
        Assert.assertEquals(XML_HEADER + expectedString, response.readEntity(String.class));
    }
}
