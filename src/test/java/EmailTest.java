import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.mail.Session;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Tests for the Email class in the Apache Commons Email package.
 */
public class EmailTest {

    private EmailConcrete email;

    /**
     * A concrete subclass of Email to allow instantiation and testing of the abstract Email class.
     */
    private class EmailConcrete extends Email {
        @Override
        public Email setMsg(String msg) throws EmailException {
            // Dummy implementation for the abstract method, for testing purposes
            return this;
        }
    }

    /**
     * Set up a new EmailConcrete instance before each test.
     */
    @Before
    public void setUp() {
        email = new EmailConcrete();
    }

    /**
     * Tear down the EmailConcrete instance after each test. Not necessary in this context, 
     * but included for completeness and future use.
     */
    @After
    public void tearDown() {
        // Not needed for this particular test class, but if resources need to be cleaned up, it would be done here.
    }

    /**
     * Test adding a BCC address.
     */
    @Test
    public void testAddBcc() throws EmailException {
        email.addBcc("bcc@example.com");
        assertEquals("BCC address should be added", 1, email.getBccAddresses().size());
    }

    /**
     * Test adding a CC address.
     */
    @Test
    public void testAddCc() throws EmailException {
        email.addCc("cc@example.com");
        assertEquals("CC address should be added", 1, email.getCcAddresses().size());
    }

    /**
     * Test adding an email header.
     * @throws MessagingException 
     */
    @Test
    public void testAddHeader() throws EmailException, MessagingException {
        email.addHeader("X-Priority", "1");
        email.addHeader("X-Mailer", "MyMailer");

        email.setHostName("localhost");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");
        email.buildMimeMessage();

        MimeMessage mimeMessage = email.getMimeMessage();
        assertEquals("Header value should match", "1", mimeMessage.getHeader("X-Priority")[0]);
        assertEquals("Header value should match", "MyMailer", mimeMessage.getHeader("X-Mailer")[0]);

    }


    /**
     * Test adding a reply-to address.
     */
    @Test
    public void testAddReplyTo() throws EmailException {
        email.addReplyTo("replyto@example.com", "ReplyToName");
        assertEquals("Reply-to address should be added", 1, email.getReplyToAddresses().size());
    }

    /**
     * Test building the MimeMessage.
     * @throws MessagingException 
     */
    @Test
    public void testBuildMimeMessage() throws EmailException, MessagingException {
        // Testing with a valid setup
        email.setHostName("localhost");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");
        email.buildMimeMessage();

        MimeMessage mimeMessage = email.getMimeMessage();
        assertNotNull("MimeMessage should be built", mimeMessage);
        assertEquals("Subject should match", "Test Subject", mimeMessage.getSubject());
        assertEquals("From should match", "from@example.com", mimeMessage.getFrom()[0].toString());
        assertNotNull("To addresses should be set", mimeMessage.getRecipients(Message.RecipientType.TO));

    }


    /**
     * Test retrieving the host name.
     */
    @Test
    public void testGetHostName() {
        assertNull("Initial host name should be null", email.getHostName());
        email.setHostName("localhost");
        assertEquals("Host name should be retrievable", "localhost", email.getHostName());
    }

    /**
     * Test getting the mail session.
     */
    @Test
    public void testGetMailSession() throws EmailException {
        Properties properties = System.getProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "localhost");
        properties.put("mail.smtp.port", "25");

        Session session = Session.getDefaultInstance(properties);
        email.setMailSession(session);

        Session retrievedSession = email.getMailSession();
        assertNotNull("Mail session should be retrievable", retrievedSession);
        assertEquals("SMTP host should match", "localhost", retrievedSession.getProperty("mail.smtp.host"));
    }


    /**
     * Test setting and getting the sent date.
     */
    @Test
    public void testGetSentDate() {
        Date sentDate = new Date();
        email.setSentDate(sentDate);
        assertEquals("Sent date should be set and get correctly", sentDate, email.getSentDate());
    }

    /**
     * Test getting the socket connection timeout.
     */
    @Test
    public void testGetSocketConnectionTimeout() {
        int timeout = 25000;
        email.setSocketConnectionTimeout(timeout);
        assertEquals("Socket connection timeout should be retrievable", timeout, email.getSocketConnectionTimeout());
    }

    /**
     * Test setting the from address.
     */
    @Test
    public void testSetFrom() throws EmailException {
        email.setFrom("from@example.com");
        assertEquals("From address should be set", "from@example.com", email.getFromAddress().toString());
    }
}

