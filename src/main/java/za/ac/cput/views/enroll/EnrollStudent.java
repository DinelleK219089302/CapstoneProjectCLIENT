package za.ac.cput.views.enroll;

import com.google.gson.Gson;
import okhttp3.*;
import za.ac.cput.entity.person.Student;
import za.ac.cput.entity.tertiaryInstitution.Enroll;
import za.ac.cput.factory.person.StudentFactory;
import za.ac.cput.factory.tertiaryInstitution.EnrollFactory;
import za.ac.cput.views.student.StudentMainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class EnrollStudent extends JFrame implements ActionListener {
    public static final MediaType JSON =
            MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();


    private JLabel lblStudentID, lblCourseCode, lblDate, lblPaymentReceived;
    private JTextField txtStudentID, txtCourseCode,txtDate, txtPaymentReceived;


    private JButton btnSave, btnCancel;
    public JPanel pN, pS;

    public EnrollStudent(){
        super("Enroll Student");
        pN = new JPanel();
        pS = new JPanel();

        lblStudentID = new JLabel("Student ID: ");
        lblCourseCode = new JLabel("Course Code: ");
        lblDate = new JLabel("Date: ");
        lblPaymentReceived = new JLabel("Payment Received: ");

        txtStudentID = new JTextField(30);
        txtCourseCode = new JTextField(30);
        txtDate = new JTextField(30);
        txtPaymentReceived = new JTextField(30);


        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");


    }

    public void setGUI(){
        pN.setLayout(new GridLayout(0,2));
        pS.setLayout(new GridLayout(1,2));

        pN.add(lblStudentID);
        pN.add(lblCourseCode);
        pN.add(lblDate);
        pN.add(lblPaymentReceived);
        pN.add(txtStudentID);
        pN.add(txtCourseCode);
        pN.add(txtDate);
        pN.add(txtPaymentReceived);

        pS.add(btnSave);
        pS.add(btnCancel);

        this.add(pN, BorderLayout.NORTH);
        this.add(pS, BorderLayout.SOUTH);

        btnSave.addActionListener(this);
        btnCancel.addActionListener(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            store(txtStudentID.getText(),
                    txtCourseCode.getText(),
                    txtDate.getText(),
                    txtPaymentReceived.getText());

        } else if (e.getSource() == btnCancel) {
            EnrollMainGUI.main(null);
            this.setVisible(false);
        }
    }

    public void store(String studentID, String courseCode, String date, String paymentReceived) {
        try {
            final String URL = "http://localhost:8080/enroll/create";

            Enroll enroll = EnrollFactory.build(studentID, courseCode,date, paymentReceived);
            Gson g = new Gson();
            String jsonString = g.toJson(enroll);
            String r = post(URL, jsonString);
            if (r != null) {
                JOptionPane.showMessageDialog(null, "Student Enrollment Complete");
                EnrollMainGUI.main(null);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Student could not be enrolled");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public String post(final String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) {
        new EnrollStudent().setGUI();
    }
}



