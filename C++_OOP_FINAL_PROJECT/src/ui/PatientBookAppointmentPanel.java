package ui;

import dao.AppointmentDAO;
import dao.UserDAO;
import model.Appointment;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class PatientBookAppointmentPanel extends JPanel {
    private final User me;
    private final UserDAO userDAO = new UserDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    private final JComboBox<String> doctorBox = new JComboBox<>();
    private final JTextField datetimeField = new JTextField("yyyy-mm-dd HH:MM:SS",20);
    private final JTextArea notesArea = new JTextArea(4, 30);

    public PatientBookAppointmentPanel(User me) {
        this.me = me;
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4,2,8,8));
        loadDoctors();
        form.add(new JLabel("Choose Doctor:")); form.add(doctorBox);
        form.add(new JLabel("DateTime (yyyy-mm-dd HH:MM:SS):")); form.add(datetimeField);
        form.add(new JLabel("Notes:")); form.add(new JScrollPane(notesArea));
        JButton book = new JButton("Book Appointment");
        form.add(new JLabel()); form.add(book);
        add(form, BorderLayout.NORTH);
        book.addActionListener(e -> book());
    }

    private void loadDoctors() {
        doctorBox.removeAllItems();
        List<User> doctors = userDAO.getUsersByRole("DOCTOR");
        for (User d : doctors) doctorBox.addItem(d.getUsername());
    }

    private void book() {
        String doctor = (String) doctorBox.getSelectedItem();
        String dt = datetimeField.getText().trim();
        String notes = notesArea.getText().trim();
        if (doctor == null || dt.isEmpty()) { JOptionPane.showMessageDialog(this,"Select doctor and date"); return; }
        try {
            Appointment a = new Appointment();
            a.setPatientUsername(me.getUsername());
            a.setDoctorUsername(doctor);
            a.setAppointmentDate(Timestamp.valueOf(dt));
            a.setStatus("PENDING");
            a.setNotes(notes);
            if (appointmentDAO.addAppointment(a)) { JOptionPane.showMessageDialog(this,"Appointment booked"); }
            else JOptionPane.showMessageDialog(this,"Booking failed");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"Invalid date format. Use yyyy-mm-dd HH:MM:SS");
        }
    }
}
