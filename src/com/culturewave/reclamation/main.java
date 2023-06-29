package com.culturewave.reclamation;

import static com.codename1.ui.CN.*;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.PickerComponent;
import com.codename1.ui.TextComponent;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.TextModeLayout;
import com.codename1.ui.validation.GroupConstraint;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import com.codename1.io.NetworkEvent;
import com.codename1.db.*;
import com.codename1.ui.Display;
import com.codename1.ui.TextField;
import com.mysql.jdbc.Connection;
import java.io.IOException;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename
 * One</a> for the purpose of building native mobile applications using Java.
 */
public class main {

    private Form current;
    private Resources theme;

    private Form home;
    private Database db;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if (err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });

    }

    public void ajoutrec() {

        Form insertForm = new Form("Insert Reclamation", BoxLayout.y());

        // Create text fields for input
        TextField typeField = new TextField("", "Type", 20, TextField.ANY);
        TextField contenuField = new TextField("", "Contenu", 20, TextField.ANY);
        TextField dateprobField = new TextField("", "Date Prob", 20, TextField.ANY);
        TextField idreclamateurField = new TextField("", "ID Reclamateur", 20, TextField.ANY);
        TextField idciblereclamationField = new TextField("", "ID Cible Reclamation", 20, TextField.ANY);

// Create button for submitting the form
        Button submitButton = new Button("Submit");

// Add the components to the form
        insertForm.addAll(typeField, contenuField, dateprobField, idreclamateurField, idciblereclamationField, submitButton);

// When the submit button is pressed, insert data into the table
        submitButton.addActionListener(e -> {
            try {
                // Get the values from the text fields
                String type = typeField.getText();
                String contenu = contenuField.getText();
                String dateprob = dateprobField.getText();
                int idreclamateur = Integer.parseInt(idreclamateurField.getText());
                int idciblereclamation = Integer.parseInt(idciblereclamationField.getText());

                // Execute the insert statement
                db.execute("insert into reclamation (type, contenu, dateprob, idreclamateur, idciblereclamation) values (?, ?, ?, ?, ?);",
                        new Object[]{type, contenu, dateprob, idreclamateur, idciblereclamation});
                ToastBar.showInfoMessage("Data inserted successfully!");
            } catch (Exception ex) {
                ToastBar.showErrorMessage("Error inserting data: " + ex.getMessage());
            }
        });

        insertForm.show();

    }

    public void start() {

        if (current != null) {
            current.show();
            return;
        }
        try {
            db = Database.openOrCreate("culturewave.db");
            db.execute("create table if not exists reclamation (id INTEGER PRIMARY KEY AUTOINCREMENT ,type TEXT ,contenu TEXT, dateprob DATE , idreclamateur INTEGERR, idciblereclamation INTEGER);");
            System.out.println("table crée");
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    public void stop() {
        current = getCurrentForm();
        if (current instanceof Dialog) {
            ((Dialog) current).dispose();
            current = getCurrentForm();
        }
    }

    public void destroy() {
    }

}
