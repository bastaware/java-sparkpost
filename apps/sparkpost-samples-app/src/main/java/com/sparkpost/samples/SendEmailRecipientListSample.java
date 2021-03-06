
package com.sparkpost.samples;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;
import com.sparkpost.model.AddressAttributes;
import com.sparkpost.model.StoredRecipientList;
import com.sparkpost.model.TemplateContentAttributes;
import com.sparkpost.model.TransmissionWithRecipientList;
import com.sparkpost.model.responses.Response;
import com.sparkpost.resources.ResourceTransmissions;
import com.sparkpost.sdk.samples.helpers.SparkPostBaseApp;
import com.sparkpost.transport.RestConnection;

public class SendEmailRecipientListSample extends SparkPostBaseApp {

    static final Logger logger = Logger.getLogger(CreateTemplateSimple.class);

    private Client client;

    public static void main(String[] args) throws SparkPostException, IOException {
        Logger.getRootLogger().setLevel(Level.DEBUG);

        SendEmailRecipientListSample sample = new SendEmailRecipientListSample();
        sample.runApp();
    }

    private void runApp() throws SparkPostException, IOException {
        this.client = this.newConfiguredClient();

        // Loads an email to send from the file system
        String fromAddress = getFromAddress();
        String[] recipients = getTestRecipients();

        sendEmail(fromAddress, recipients);

    }

    private void sendEmail(String from, String[] recipients) throws SparkPostException {

        // CHANGE THIS TO ONE OF YOUR TEMPLATE IDs
        final String YOUR_RECIPIENT_LIST_ID = "30115044363775968";

        TransmissionWithRecipientList transmission = new TransmissionWithRecipientList();
        StoredRecipientList recipiList = new StoredRecipientList();
        recipiList.setListId(YOUR_RECIPIENT_LIST_ID);
        transmission.setRecipientList(recipiList);

        // Populate Substitution Data
        Map<String, Object> substitutionData = new HashMap<String, Object>();
        substitutionData.put("yourContent", "You can add substitution data too.");
        transmission.setSubstitutionData(substitutionData);

        // Populate Email Body
        TemplateContentAttributes contentAttributes = new TemplateContentAttributes();
        contentAttributes.setFrom(new AddressAttributes(from));
        contentAttributes.setSubject("Your subject content here. {{yourContent}}");
        contentAttributes.setText("Your Text content here.  {{yourContent}}");
        contentAttributes.setHtml("<p>Your <b>HTML</b> content here.  {{yourContent}}</p>");
        transmission.setContentAttributes(contentAttributes);

        transmission.setContentAttributes(contentAttributes);

        // Send the Email
        RestConnection connection = new RestConnection(this.client, getEndPoint());
        Response response = ResourceTransmissions.create(connection, 0, transmission);

        logger.debug("Transmission Response: " + response);
    }

}
