import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {

        JFrame frame = new JFrame("Dairy Management System");
        frame.setLayout(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "Vedant", "Vedant@7101");
        Statement statement = connection.createStatement();

        JTabbedPane tabbedPane = new JTabbedPane();

        frame.setContentPane(tabbedPane);
        frame.setVisible(true);

        int width = frame.getWidth(), height = frame.getHeight();

        tabbedPane.addTab("Add Customer", new addCustomer(frame, statement, width, height));
        tabbedPane.addTab("Add Record", new addRecord(frame, statement, width, height));
        tabbedPane.addTab("Update Record", new updateRecord(frame, statement, width, height));
        tabbedPane.addTab("Delete Record", new deleteRecord(frame, statement, width, height));
        tabbedPane.addTab("Rate", new Rate(frame, statement, width, height));
        tabbedPane.addTab("Advance and Cattle Feed", new Advance(frame, statement, width, height));
        tabbedPane.addTab("Payment", new Payment(frame, statement, width, height));
        tabbedPane.addTab("Payment Slip", new PaymentSlip(frame, statement, width, height));
    }
}


class addCustomer extends JPanel implements ActionListener {

    JTextField textField1, textField2;
    JButton button;

    addCustomer(JFrame frame, Statement statement, int width, int height) {

        JLabel name = new TabName("Add Customer", width, height/2-137);

        JPanel panel = new JPanel();
        panel.setBounds(width/2-250, height/2-137, 500, 275);
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);

        JLabel label1 = new JRoundedLabel("Name");
        label1.setBounds(100, 50, 100, 30);
        textField1 = new JTextField();
        textField1.setBounds(250, 50, 150, 30);
        textField1.addActionListener(this);
        JLabel label2 = new JRoundedLabel("Customer ID");
        label2.setBounds(100, 125, 100, 30);
        textField2 = new JTextField();
        textField2.setBounds(250, 125, 150, 30);
        textField2.addActionListener(this);
        button = new JRoundedButton("Add Customer");
        button.setBounds(150, 200, 175, 30);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {

                if (textField1.getText().length() == 0 || textField2.getText().length() == 0) {
                    JOptionPane.showMessageDialog(frame, "Please Fill Details");
                    return;
                }
                String name = textField1.getText();
                String id1 = textField2.getText();
                int id2 = Integer.parseInt(id1);
                try {
                    String type = (new checkType()).chechType(id2);
                    statement.executeUpdate("insert into customer values('"+name+"',"+id2+",'"+type+"')");
                    statement.executeUpdate("insert into advance values("+id2+",0,0)");
                    statement.executeUpdate("insert into slipadvance values("+id2+",0,0)");
                    textField1.setText(null);
                    textField2.setText(null);
                }
                catch (SQLException e1) {
                }
            }
        });

        panel.add(label1);
        panel.add(label2);
        panel.add(textField1);
        panel.add(textField2);
        panel.add(button);

        setBackground(Color.WHITE);
        setLayout(null);
        add(name);
        add(panel);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == textField1) {
            textField2.requestFocus();
        }
        if (e.getSource() == textField2) {
            button.doClick();
            textField1.requestFocus();
        }
    }
}

class addRecord extends JPanel implements ActionListener {

    JTextField textField1, textField2, textField3, textField4, textField5, textField6;
    JButton button2, button3;

    addRecord(JFrame frame, Statement statement, int width, int height) {

        JLabel name = new TabName("Add Record", width, height/2-212);
        JButton button1 = new JRoundedButton("Previous");

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);
        panel1(panel, frame, statement, button1, width, height);

        button1.setBounds(width/2-50, height/2+252, 100, 30);
        button1.setVisible(false);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {

                panel.removeAll();
                panel.repaint();
                panel1(panel, frame, statement, button1, width, height);
            }
        });

        setBackground(Color.WHITE);
        setLayout(null);
        add(name);
        add(panel);
        add(button1);
    }

    private void panel1(JPanel panel, JFrame frame, Statement statement, JButton button, int width, int height) {

        JLabel label1 = new JRoundedLabel("Date");
        label1.setBounds(100, 50, 100, 30);
        textField1 = new JTextField();
        textField1.setBounds(250, 50, 150, 30);
        textField1.addActionListener(this);
        JLabel label2 = new JRoundedLabel("Day");
        label2.setBounds(100, 125, 100, 30);
        textField2 = new JTextField();
        textField2.setBounds(250, 125, 150, 30);
        textField2.addActionListener(this);
        button2 = new JRoundedButton("Add Day");
        button2.setBounds(150, 200, 175, 30);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {

                String date = textField1.getText();
                String day = textField2.getText().toUpperCase();
                panel.removeAll();
                panel.repaint();
                panel2(panel, frame, statement, button, date, day, width, height);
            }
        });

        button.setVisible(false);

        panel.setBounds(width/2-250, height/2-137, 500, 275);
        panel.add(label1);
        panel.add(label2);
        panel.add(textField1);
        panel.add(textField2);
        panel.add(button2);
    }

    private void panel2(JPanel panel, JFrame frame, Statement statement, JButton button, String date, String day, int width, int height) {

        JLabel label3 = new JRoundedLabel("Customer ID");
        label3.setBounds(100, 50, 100, 30);
        textField3 = new JTextField();
        textField3.setBounds(250, 50, 150, 30);
        textField3.addActionListener(this);
        JLabel label4 = new JRoundedLabel("Liter");
        label4.setBounds(100, 125, 100, 30);
        textField4 = new JTextField();
        textField4.setBounds(250, 125, 150, 30);
        textField4.addActionListener(this);
        JLabel label5 = new JRoundedLabel("Degree");
        label5.setBounds(100, 200, 100, 30);
        textField5 = new JTextField();
        textField5.setBounds(250, 200, 150, 30);
        textField5.addActionListener(this);
        JLabel label6 = new JRoundedLabel("Fat");
        label6.setBounds(100, 275, 100, 30);
        textField6 = new JTextField();
        textField6.setBounds(250, 275, 150, 30);
        textField6.addActionListener(this);
        button3 = new JRoundedButton("Add Record");
        button3.setBounds(150, 350, 175, 30);

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {

                int id = Integer.parseInt(textField3.getText());
                float liter = Float.parseFloat(textField4.getText());
                Float degree = Float.parseFloat(textField5.getText());
                Float fat = Float.parseFloat(textField6.getText());
                String type = (new checkType()).chechType(id);
                float snf =(float) ((degree / 4) + (fat * 0.21) + 0.36);
                float rate = 0;

                textField3.setText(null);
                textField4.setText(null);
                textField5.setText(null);
                textField6.setText(null);

                if (type == "Cow") {
                    try {
                        ResultSet resultSet= statement.executeQuery("select * from cowrate");
                        while (resultSet.next()) {
                            Float degree1 = resultSet.getFloat(1);
                            Float fat1 = resultSet.getFloat(2);
                            if (degree.equals(degree1) && fat.equals(fat1)) {
                                rate = resultSet.getFloat(3);
                            }
                        }
                    } catch (SQLException e1) {
                    }
                }
                if (type == "Buffalo") {
                    try {
                        ResultSet resultSet= statement.executeQuery("select * from buffalorate");
                        while (resultSet.next()) {
                            Float degree1 = resultSet.getFloat(1);
                            Float fat1 = resultSet.getFloat(2);
                            if (degree.equals(degree1) && fat.equals(fat1)) {
                                rate = resultSet.getFloat(3);
                            }
                        }
                    } catch (SQLException e1) {
                    }
                }

                if (rate == 0) {
                    JOptionPane.showMessageDialog(frame, "Rate not found.");
                    return;
                }

                float amount = (rate * liter);

                try {
                    statement.executeUpdate("insert into record values(to_date('"+date+"','dd/mm/yyyy'), '"+day+"', '"+type+"', "+id+", "+liter+", "+fat+", "+degree+", "+snf+", "+rate+", "+amount+")");
                } catch (SQLException e1) {
                }
            }
        });

        button.setVisible(true);

        panel.setBounds(width/2-250, height/2-212, 500, 425);
        panel.add(label3);
        panel.add(label4);
        panel.add(label5);
        panel.add(label6);
        panel.add(textField3);
        panel.add(textField4);
        panel.add(textField5);
        panel.add(textField6);
        panel.add(button3);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == textField1) {
            textField2.requestFocus();
        }
        if (e.getSource() == textField2) {
            button2.doClick();
        }
        if (e.getSource() == textField3) {
            textField4.requestFocus();
        }
        if (e.getSource() == textField4) {
            textField5.requestFocus();
        }
        if (e.getSource() == textField5) {
            textField6.requestFocus();
        }
        if (e.getSource() == textField6) {
            button3.doClick();
            textField3.requestFocus();
        }
    }
}

class deleteRecord extends JPanel implements ActionListener {

    JTextField textField1, textField2, textField3;
    JButton button1;

    deleteRecord(JFrame frame, Statement statement, int width, int height) {

        JLabel name = new TabName("Delete Record", width, height/2-175);

        JPanel panel = new JPanel();
        panel.setBounds(width/2-250, height/2-175, 500, 350);
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);

        JLabel label1 = new JRoundedLabel("Date");
        label1.setBounds(100, 50, 100, 30);
        textField1 = new JTextField();
        textField1.setBounds(250, 50, 150, 30);
        textField1.addActionListener(this);
        JLabel label2 = new JRoundedLabel("Day");
        label2.setBounds(100, 125, 100, 30);
        textField2 = new JTextField();
        textField2.setBounds(250, 125, 150, 30);
        textField2.addActionListener(this);
        JLabel label3 = new JRoundedLabel("Customer ID");
        label3.setBounds(100, 200, 100, 30);
        textField3 = new JTextField();
        textField3.setBounds(250, 200, 150, 30);
        textField3.addActionListener(this);
        button1 = new JRoundedButton("Delete Record");
        button1.setBounds(150, 275, 175, 30);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {

                if (textField1.getText().length() == 0 || textField2.getText().length() == 0 || textField3.getText().length() == 0) {
                    JOptionPane.showMessageDialog(frame, "Please fill details.");
                    return;
                }

                String date = textField1.getText();
                String day = textField2.getText().toUpperCase();
                int id = Integer.parseInt(textField3.getText());
                try {
                    statement.executeUpdate("delete from record where dt=to_date('"+date+"','dd/mm/yyyy') and day='"+day+"' and id="+id);
                    textField1.setText(null);
                    textField2.setText(null);
                    textField3.setText(null);
                } catch (SQLException e1) {
                }
            }
        });

        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(textField1);
        panel.add(textField2);
        panel.add(textField3);
        panel.add(button1);

        setBackground(Color.WHITE);
        setLayout(null);
        add(name);
        add(panel);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == textField1) {
            textField2.requestFocus();
        }
        if (e.getSource() == textField2) {
            textField3.requestFocus();
        }
        if (e.getSource() == textField3) {
            button1.doClick();
            textField1.requestFocus();
        }
    }
}

class updateRecord extends JPanel implements ActionListener {

    JTextField textField1, textField2, textField3, textField4, textField5, textField6;
    JButton button1;

    updateRecord(JFrame frame, Statement statement, int width, int height) {

        JLabel name = new TabName("Update Record", width, height/2-287);

        JPanel panel = new JPanel();
        panel.setBounds(width/2-250, height/2-287, 500, 575);
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);

        JLabel label1 = new JRoundedLabel("Date");
        label1.setBounds(100, 50, 100, 30);
        textField1 = new JTextField();
        textField1.setBounds(250, 50, 150, 30);
        textField1.addActionListener(this);
        JLabel label2 = new JRoundedLabel("Day");
        label2.setBounds(100, 125, 100, 30);
        textField2 = new JTextField();
        textField2.setBounds(250, 125, 150, 30);
        textField2.addActionListener(this);
        JLabel label3 = new JRoundedLabel("Customer ID");
        label3.setBounds(100, 200, 100, 30);
        textField3 = new JTextField();
        textField3.setBounds(250, 200, 150, 30);
        textField3.addActionListener(this);
        JLabel label4 = new JRoundedLabel("Liter");
        label4.setBounds(100, 275, 100, 30);
        textField4 = new JTextField();
        textField4.setBounds(250, 275, 150, 30);
        textField4.addActionListener(this);
        JLabel label5 = new JRoundedLabel("Degree");
        label5.setBounds(100, 350, 100, 30);
        textField5 = new JTextField();
        textField5.setBounds(250, 350, 150, 30);
        textField5.addActionListener(this);
        JLabel label6 = new JRoundedLabel("Fat");
        label6.setBounds(100, 425, 100, 30);
        textField6 = new JTextField();
        textField6.setBounds(250, 425, 150, 30);
        textField6.addActionListener(this);
        button1 = new JRoundedButton("Update Record");
        button1.setBounds(150, 500, 175, 30);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {

                if (textField1.getText().length() == 0 || textField2.getText().length() == 0 || textField3.getText().length() == 0 || textField4.getText().length() == 0 || textField5.getText().length() == 0 || textField6.getText().length() == 0) {
                    JOptionPane.showMessageDialog(frame, "Please fill details");
                    return;
                }

                String date = textField1.getText();
                String day = textField2.getText().toUpperCase();
                int id = Integer.parseInt(textField3.getText());
                Float liter = Float.parseFloat(textField4.getText());
                Float degree = Float.parseFloat(textField5.getText());
                Float fat = Float.parseFloat(textField6.getText());
                float snf =(float) ((degree / 4) + (fat * 0.21) + 0.36);
                float rate = 25;
                float amount = (rate * liter);
                try {
                    statement.executeUpdate("update record set liter="+liter+", fat="+degree+", degree="+fat+", snf="+snf+", rate="+rate+", amount="+amount+" where dt=to_date('"+date+"','dd/mm/yyyy') and day='"+day+"' and id="+id);
                    textField1.setText(null);
                    textField2.setText(null);
                    textField3.setText(null);
                    textField4.setText(null);
                    textField5.setText(null);
                    textField6.setText(null);
                } catch (SQLException e1) {
                }
            }
        });

        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(label4);
        panel.add(label5);
        panel.add(label6);
        panel.add(textField1);
        panel.add(textField2);
        panel.add(textField3);
        panel.add(textField4);
        panel.add(textField5);
        panel.add(textField6);
        panel.add(button1);

        setBackground(Color.WHITE);
        setLayout(null);
        add(name);
        add(panel);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == textField1) {
            textField2.requestFocus();
        }
        if (e.getSource() == textField2) {
            textField3.requestFocus();
        }
        if (e.getSource() == textField3) {
            textField4.requestFocus();
        }
        if (e.getSource() == textField4) {
            textField5.requestFocus();
        }
        if (e.getSource() == textField5) {
            textField6.requestFocus();
        }
        if (e.getSource() == textField6) {
            button1.doClick();
            textField1.requestFocus();
        }
    }
}

class Rate extends JPanel implements ActionListener {

    JTextField textField1, textField2, textField3;
    JButton button1;

    Rate(JFrame frame, Statement statement, int width, int height) {

        JLabel name = new TabName("Rate", width, height/2-325);

        JPanel panel = new JPanel();
        panel.setBounds(width/2-250, height/2-325, 500, 650);
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);

        JRadioButton cow = new JRoundedRadioButton("Cow");
        cow.setBounds(100, 50, 100, 30);
        JRadioButton buffalo = new JRoundedRadioButton("Buffalo");
        buffalo.setBounds(250, 50, 100, 30);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(cow);
        buttonGroup.add(buffalo);

        JLabel label1 = new JRoundedLabel("Degree");
        label1.setBounds(100, 125, 100, 30);
        textField1 = new JTextField();
        textField1.setBounds(250, 125, 150, 30);
        textField1.addActionListener(this);
        JLabel label2 = new JRoundedLabel("Fat");
        label2.setBounds(100, 200, 100, 30);
        textField2 = new JTextField();
        textField2.setBounds(250, 200, 150, 30);
        textField2.addActionListener(this);
        JLabel label3 = new JRoundedLabel("Rate");
        label3.setBounds(100, 275, 100, 30);
        textField3 = new JTextField();
        textField3.setBounds(250, 275, 150, 30);
        textField3.addActionListener(this);
        button1 = new JRoundedButton("Add Rate");
        button1.setBounds(150, 350, 175, 30);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {

                if (!cow.isSelected() && !buffalo.isSelected()) {
                    JOptionPane.showMessageDialog(frame, "Please select milk type.");
                    return;
                }

                if (textField1.getText().length() == 0 || textField2.getText().length() == 0 || textField3.getText().length() == 0) {
                    JOptionPane.showMessageDialog(frame, "Please Fill Details");
                    return;
                }

                Float degree = Float.valueOf(textField1.getText());
                Float fat = Float.valueOf(textField2.getText());
                Float rate = Float.valueOf(textField3.getText());

                textField1.setText(null);
                textField2.setText(null);
                textField3.setText(null);

                if (cow.isSelected()) {
                    try {
                        ResultSet resultSet= statement.executeQuery("select * from cowrate");
                        while (resultSet.next()) {
                            Float degree1 = resultSet.getFloat(1);
                            Float fat1 = resultSet.getFloat(2);
                            if (degree.equals(degree1) && fat.equals(fat1)) {
                                statement.executeUpdate("update cowrate set rate="+rate+" where degree="+degree+" and fat="+fat+"");
                                return;
                            }
                        }
                        statement.executeUpdate("insert into cowrate values("+degree+","+fat+","+rate+")");
                    } catch (SQLException e1) {
                    }
                }
                if (buffalo.isSelected()) {
                    try {
                        ResultSet resultSet= statement.executeQuery("select * from buffalorate");
                        while (resultSet.next()) {
                            Float degree1 = resultSet.getFloat(1);
                            Float fat1 = resultSet.getFloat(2);
                            if (degree.equals(degree1) && fat.equals(fat1)) {
                                statement.executeUpdate("update buffalorate set rate="+rate+" where degree="+degree+" and fat="+fat+"");
                                return;
                            }
                        }
                        statement.executeUpdate("insert into buffalorate values("+degree+","+fat+","+rate+")");
                    } catch (SQLException e1) {
                    }
                }
            }
        });

        JTable table = new JTable();
        int h = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        int v = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
        JScrollPane jScrollPane = new JScrollPane(table, v, h);
        jScrollPane.setBounds(100, 425, 300, 200);
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
        String column[] = new String[] {"Degree","Fat","Rate"};
        defaultTableModel.setColumnIdentifiers(column);
        table.setModel(defaultTableModel);

        cow.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged (ItemEvent e) {
                if (cow.isSelected()) {
                    for (int i = table.getRowCount() - 1; i >= 0; i--) {
                        defaultTableModel.removeRow(i);
                    }
                    ResultSet resultSet = null;
                    try {
                        resultSet = statement.executeQuery("select * from cowrate order by degree asc, fat asc");
                        while (resultSet.next()) {
                            defaultTableModel.addRow(new Object[] {resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)});
                        }
                    } catch (SQLException e1) {
                    }
                }
            }
        });

        buffalo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged (ItemEvent e) {

                if (buffalo.isSelected()) {
                    for (int i = table.getRowCount() - 1; i >= 0; i--) {
                        defaultTableModel.removeRow(i);
                    }
                    ResultSet resultSet = null;
                    try {
                        resultSet = statement.executeQuery("select * from buffalorate order by degree asc, fat asc");
                        while (resultSet.next()) {
                            defaultTableModel.addRow(new Object[] {resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)});
                        }
                    } catch (SQLException e1) {
                    }
                }
            }
        });

        panel.add(cow);
        panel.add(buffalo);
        panel.add(label1);
        panel.add(textField1);
        panel.add(label2);
        panel.add(textField2);
        panel.add(label3);
        panel.add(textField3);
        panel.add(button1);
        panel.add(jScrollPane);

        setBackground(Color.WHITE);
        setLayout(null);
        add(name);
        add(panel);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == textField1) {
            textField2.requestFocus();
        }
        if (e.getSource() == textField2) {
            textField3.requestFocus();
        }
        if (e.getSource() == textField3) {
            button1.doClick();
            textField1.requestFocus();
        }
    }
}

class Payment extends JPanel implements ActionListener {

    JTextField textField1, textField2, textField3, textField4;
    JButton button1;
    ArrayList<Integer> customer = new ArrayList<>();
    final String[] startingDate = {null};
    final String[] endingDate = { null };

    Payment(JFrame frame, Statement statement, int width, int height) {

        final int[] count = {0};

        JLabel name = new TabName("Payment", width/2, height/2-212);

        JPanel panel1 = new JPanel();
        panel1.setBounds(width/2-650, height/2-212, 500, 425);
        panel1.setBackground(Color.BLACK);
        panel1.setLayout(null);

        JLabel label1 = new JRoundedLabel("From");
        label1.setBounds(100, 50, 100, 30);
        textField1 = new JTextField();
        textField1.setBounds(250, 50, 150, 30);
        textField1.addActionListener(this);
        JLabel label2 = new JRoundedLabel("To");
        label2.setBounds(100, 125, 100, 30);
        textField2 = new JTextField();
        textField2.setBounds(250, 125, 150, 30);
        textField2.addActionListener(this);
        JLabel label3 = new JRoundedLabel("Starting Date");
        label3.setBounds(100, 200, 100, 30);
        textField3 = new JTextField();
        textField3.setBounds(250, 200, 150, 30);
        textField3.addActionListener(this);
        JLabel label4 = new JRoundedLabel("Ending Date");
        label4.setBounds(100, 275, 100, 30);
        textField4 = new JTextField();
        textField4.setBounds(250, 275, 150, 30);
        textField4.addActionListener(this);
        button1 = new JRoundedButton("Payment");
        button1.setBounds(150, 350, 175, 30);

        panel1.add(label1);
        panel1.add(textField1);
        panel1.add(label2);
        panel1.add(textField2);
        panel1.add(label3);
        panel1.add(textField3);
        panel1.add(label4);
        panel1.add(textField4);
        panel1.add(button1);

        JPanel panel2 = new JPanel();
        panel2.setBounds(width/2, height/2-325, 600, 650);
        panel2.setBackground(Color.BLACK);
        panel2.setLayout(null);

        JLabel label5 = new JRoundedLabel("Name");
        label5.setBounds(100, 25, 100, 30);
        JTextField textField5 = new JTextField();
        textField5.setBounds(100, 75, 150, 30);
        textField5.setEditable(false);
        JLabel label6 = new JRoundedLabel("Customer ID");
        label6.setBounds(275, 25, 100, 30);
        JTextField textField6 = new JTextField();
        textField6.setBounds(275, 75, 100, 30);
        textField6.setEditable(false);
        JLabel label7 = new JRoundedLabel("Milk Type");
        label7.setBounds(400, 25, 100, 30);
        JTextField textField7 = new JTextField();
        textField7.setBounds(400, 75, 100, 30);
        textField7.setEditable(false);

        int h = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        int v = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
        String column[] = new String[] {"Date","Day","Liter","Degree","Fat","SNF","Rate","Amount"};
        JTable table = new JTable();
        table.setBackground(Color.WHITE);
        JScrollPane jScrollPane = new JScrollPane(table, v, h);
        jScrollPane.setBounds(0, 125, 600, 350);
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
        defaultTableModel.setColumnIdentifiers(column);
        table.setModel(defaultTableModel);

        JLabel label8 = new JRoundedLabel("Total Liter");
        label8.setBounds(50, 500, 100, 30);
        JTextField textField8 = new JTextField();
        textField8.setBounds(50, 550, 85, 30);
        textField8.setEditable(false);
        JLabel label9 = new JRoundedLabel("Total Amount");
        label9.setBounds(135, 500, 100, 30);
        JTextField textField9 = new JTextField();
        textField9.setBounds(135, 550, 110, 30);
        textField9.setEditable(false);
        JLabel label10 = new JRoundedLabel("Advance");
        label10.setBounds(245, 500, 100, 30);
        JTextField textField10 = new JTextField();
        textField10.setBounds(245, 550, 100, 30);
        textField10.setEditable(false);
        JLabel label11 = new JRoundedLabel("Cattle Feed");
        label11.setBounds(345, 500, 150, 30);
        JTextField textField11 = new JTextField();
        textField11.setBounds(345, 550, 135, 30);
        textField11.setEditable(false);
        JLabel label12 = new JRoundedLabel("Net Pay");
        label12.setBounds(480, 500, 100, 30);
        JTextField textField12 = new JTextField();
        textField12.setBounds(480, 550, 70, 30);
        textField12.setEditable(false);

        JButton button2 = new JRoundedButton("Previous");
        button2.setBounds(100, 600, 100, 30);
        JButton button3 = new JRoundedButton("Next");
        button3.setBounds(250, 600, 100, 30);
        JButton button4 = new JRoundedButton("Print");
        button4.setBounds(400, 600, 100, 30);

        panel2.add(label5);
        panel2.add(textField5);
        panel2.add(label6);
        panel2.add(textField6);
        panel2.add(label7);
        panel2.add(textField7);
        panel2.add(jScrollPane);
        panel2.add(button2);
        panel2.add(button3);
        panel2.add(button4);
        panel2.add(label8);
        panel2.add(textField8);
        panel2.add(label9);
        panel2.add(textField9);
        panel2.add(label10);
        panel2.add(textField10);
        panel2.add(label11);
        panel2.add(textField11);
        panel2.add(label12);
        panel2.add(textField12);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {

                if (textField1.getText().length() == 0 || textField2.getText().length() == 0 || textField3.getText().length() == 0 || textField4.getText().length() == 0) {
                    JOptionPane.showMessageDialog(frame, "Please fill details.");
                    return;
                }

                int from = Integer.parseInt(textField1.getText());
                int to = Integer.parseInt(textField2.getText());
                startingDate[0] = textField3.getText();
                endingDate[0] = textField4.getText();
                count[0] = 0;

                try {
                    ResultSet resultSet = statement.executeQuery("select id from customer where id between "+from+" and "+to+" order by id asc");
                    while (resultSet.next()) {
                        customer.add(resultSet.getInt(1));
                    }
                    if (customer.size() > 0) {
                        showTable(frame, statement, customer.get(count[0]), defaultTableModel, startingDate[0], endingDate[0], textField5, textField6, textField7, textField8, textField9, textField10, textField11, textField12);
                    }
                }
                catch (SQLException se) {
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                count[0] -= 1;
                if (count[0] == -1){
                    count[0] = customer.size()-1;
                }
                try {
                    showTable(frame, statement, customer.get(count[0]), defaultTableModel, startingDate[0], endingDate[0], textField5, textField6, textField7, textField8, textField9, textField10, textField11, textField12);
                } catch (SQLException e1) {
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                count[0] += 1;
                if (count[0] == customer.size()) {
                    count[0] = 0;
                }
                try {
                    showTable(frame, statement, customer.get(count[0]), defaultTableModel, startingDate[0], endingDate[0], textField5, textField6, textField7, textField8, textField9, textField10, textField11, textField12);
                } catch (SQLException e1) {
                }
            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveInFile(statement);
                } catch (IOException e1) {
                } catch (SQLException e1) {
                }
            }
        });

        setBackground(Color.WHITE);
        setLayout(null);
        add(name);
        add(panel1);
        add(panel2);
    }

    private void showTable(JFrame frame, Statement statement, int customerId, DefaultTableModel defaultTableModel, String startingDate, String endingDate, JTextField textField5, JTextField textField6, JTextField textField7, JTextField textField8, JTextField textField9, JTextField textField10, JTextField textField11, JTextField textField12) throws SQLException {

        String name = null, id = null, type = null;

        ResultSet resultSet = statement.executeQuery("select c.name,to_char(r.dt,'dd/mm/yyyy'),r.day,r.liter,r.fat,r.degree,r.snf,r.rate,r.amount from customer c join record r on (c.id="+customerId+" and r.id="+customerId+" and dt between to_date('"+startingDate+"','dd/mm/yyyy') and to_date('"+endingDate+"','dd/mm/yyyy')) order by r.dt asc, r.day desc");
        for (int i = defaultTableModel.getRowCount() - 1; i >= 0; i--) {
            defaultTableModel.removeRow(i);
        }
        int i = 0;
        while (resultSet.next()) {
            i++;
        }
        if (i == 0) {
            return;
        }
        resultSet = statement.executeQuery("select c.name,to_char(r.dt,'dd/mm/yyyy'),r.day,r.liter,r.fat,r.degree,r.snf,r.rate,r.amount from customer c join record r on (c.id="+customerId+" and r.id="+customerId+" and dt between to_date('"+startingDate+"','dd/mm/yyyy') and to_date('"+endingDate+"','dd/mm/yyyy')) order by r.dt asc, r.day desc");
        while (resultSet.next()) {
            name = resultSet.getString(1);
            id = String.valueOf(customerId);
            type = new checkType().chechType(customerId);
            String date = resultSet.getString(2);
            String day = resultSet.getString(3);
            String liter = resultSet.getString(4);
            String fat = resultSet.getString(5);
            String degree = resultSet.getString(6);
            String snf = resultSet.getString(7);
            String rate = resultSet.getString(8);
            String amount = resultSet.getString(9);
            defaultTableModel.addRow(new Object[] {date,day,liter,degree,fat,snf,rate,amount});
        }
        textField5.setText(name);
        textField6.setText(id);
        textField7.setText(type);
        String totalAmount = null, totalLiter = null;
        resultSet = statement.executeQuery("select sum(liter), sum(amount) from record where id="+customerId+" and dt between to_date('"+startingDate+"','dd/mm/yyyy') and to_date('"+endingDate+"','dd/mm/yyyy')");
        while (resultSet.next()) {
            totalLiter = resultSet.getString(1);
            totalAmount = resultSet.getString(2);
        }
        resultSet = statement.executeQuery("select * from advance where id="+id);
        while (resultSet.next()) {
            int advance = Integer.parseInt(resultSet.getString(2));
            int cattle = Integer.parseInt(resultSet.getString(3));
            int net = Integer.parseInt(totalAmount) - advance - cattle - 1;
            textField8.setText(totalLiter);
            textField9.setText(totalAmount);
            textField10.setText(String.valueOf(advance));
            textField11.setText(String.valueOf(cattle));
            textField12.setText(String.valueOf(net));
        }
    }

    private void saveInFile (Statement statement) throws IOException, SQLException {

        FileWriter fileWriter = new FileWriter(new File("dairy.txt"));

        for (int x:customer) {

            String name = null, id = String.valueOf(x), type = null, totalAmount = null, totalLiter = null, advance = null, cattle = null, net = null;

            ResultSet resultSet = statement.executeQuery("select c.name,to_char(r.dt,'dd/mm/yyyy'),r.day,r.liter,r.fat,r.degree,r.snf,r.rate,r.amount from customer c join record r on (c.id="+id+" and r.id="+id+" and dt between to_date('"+startingDate[0]+"','dd/mm/yyyy') and to_date('"+endingDate[0]+"','dd/mm/yyyy')) order by r.dt asc, r.day desc");
            int count = 0;
            while (resultSet.next()) {
                count++;
            }
            if (count == 0) {
                return;
            }
            resultSet = statement.executeQuery("select * from customer where id="+id);
            while (resultSet.next()) {
                name = resultSet.getString(1);
                type = new checkType().chechType(Integer.parseInt(id));
            }
            fileWriter.append("                                 PADALKAR ENTERPRISES                                  " + System.lineSeparator());
            fileWriter.append("=======================================================================================" + System.lineSeparator());
            fileWriter.append("Customer Number      : " + id);
            for (int i = 0; i < 25 - id.length(); i++)
                fileWriter.append(" ");
            fileWriter.append("Name        : " + name + System.lineSeparator());
            fileWriter.append("Type                 : " + type);
            for (int i = 0; i < 25 - type.length(); i++)
                fileWriter.append(" ");
            fileWriter.append("Period From : " +startingDate[0]+ " to " + endingDate[0] + System.lineSeparator());
            fileWriter.append("=======================================================================================" + System.lineSeparator());
            fileWriter.append("=======================================================================================" + System.lineSeparator());
            fileWriter.append("   DATE         M/E      Litre      Fat      Degree      SNF      Rate      Amount     " + System.lineSeparator());
            fileWriter.append("=======================================================================================" + System.lineSeparator());
            resultSet = statement.executeQuery("select to_char(r.dt,'dd/mm/yyyy'),r.day,r.liter,r.fat,r.degree,r.snf,r.rate,r.amount from record r where r.id="+id+" and dt between to_date('"+startingDate[0]+"','dd/mm/yyyy') and to_date('"+endingDate[0]+"','dd/mm/yyyy') order by r.dt asc, r.day desc");
            while (resultSet.next()) {
                String date = resultSet.getString(1);
                String day = resultSet.getString(2);
                String liter = resultSet.getString(3);
                String fat = resultSet.getString(4);
                String degree = resultSet.getString(5);
                String snf = resultSet.getString(6);
                String rate = resultSet.getString(7);
                String amount = resultSet.getString(8);
                fileWriter.append("   " + date + "   " + day + "        " + liter);
                for (int i = 0; i < 11 - liter.length(); i++)
                    fileWriter.append(" ");
                fileWriter.append(fat);
                for (int i = 0; i < 9 - fat.length(); i++)
                    fileWriter.append(" ");
                fileWriter.append(degree);
                for (int i = 0; i < 12 - degree.length(); i++)
                    fileWriter.append(" ");
                fileWriter.append(snf);
                for (int i = 0; i < 9 - snf.length(); i++)
                    fileWriter.append(" ");
                fileWriter.append(rate);
                for (int i = 0; i < 10 - rate.length(); i++)
                    fileWriter.append(" ");
                fileWriter.append(amount + System.lineSeparator());
            }
            resultSet = statement.executeQuery("select sum(liter), sum(amount) from record where id="+id+" and dt between to_date('"+startingDate[0]+"','dd/mm/yyyy') and to_date('"+endingDate[0]+"','dd/mm/yyyy')");
            while (resultSet.next()) {
                totalLiter = resultSet.getString(1);
                totalAmount = resultSet.getString(2);
            }
            resultSet = statement.executeQuery("select * from advance where id="+id);
            while (resultSet.next()) {
                int advanceInt = Integer.parseInt(resultSet.getString(2));
                int cattleInt = Integer.parseInt(resultSet.getString(3));
                int netInt = Integer.parseInt(totalAmount) - advanceInt - cattleInt - 1;
                advance = String.valueOf(advanceInt);
                cattle = String.valueOf(cattleInt);
                net = String.valueOf(netInt);
                if (netInt < 0) {
                    statement.executeUpdate("update advance set advance="+(-netInt)+", cattle=0 where id=" + id);
                }
                else {
                    statement.executeUpdate("update advance set advance=0, cattle=0 where id=" + id);
                }
            }
            fileWriter.append("=======================================================================================" + System.lineSeparator());
            fileWriter.append("                                                         Total Liter     : " + totalLiter + System.lineSeparator());
            fileWriter.append("                                                         Total Amount    : " + totalAmount + System.lineSeparator());
            fileWriter.append("                                                         Advance Amount  : " + advance + System.lineSeparator());
            fileWriter.append("                                                         Cattle Feed     : " + cattle + System.lineSeparator());
            fileWriter.append("                                                         Billing Charge  : 1" + System.lineSeparator());
            fileWriter.append("                                                         Net Pay         : " + net + System.lineSeparator());
            fileWriter.append("=======================================================================================");
            for (int k = 0; k < 57 - count - 16; k++) {
                fileWriter.append(System.lineSeparator());
            }
        }
        fileWriter.close ();
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == textField1) {
            textField2.requestFocus();
        }
        if (e.getSource() == textField2) {
            textField3.requestFocus();
        }
        if (e.getSource() == textField3) {
            textField4.requestFocus();
        }
        if (e.getSource() == textField4) {
            button1.doClick();
            textField1.requestFocus();
        }
    }
}

class Advance extends JPanel implements ActionListener {

    JTextField textField1, textField2, textField3;
    JButton button1;

    Advance(JFrame frame, Statement statement, int width, int height) {

        JLabel name = new TabName("Advance and Cattle Feed", width, height/2-175);

        JPanel panel = new JPanel();
        panel.setBounds(width/2-250, height/2-175, 500, 350);
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);

        JLabel label1 = new JRoundedLabel("Customer ID");
        label1.setBounds(100, 50, 100, 30);
        textField1 = new JTextField();
        textField1.setBounds(250, 50, 150, 30);
        textField1.addActionListener(this);
        JLabel label2 = new JRoundedLabel("Advance");
        label2.setBounds(100, 125, 100, 30);
        textField2 = new JTextField();
        textField2.setBounds(250, 125, 150, 30);
        textField2.addActionListener(this);
        JLabel label3 = new JRoundedLabel("Cattle Feed");
        label3.setBounds(100, 200, 100, 30);
        textField3 = new JTextField();
        textField3.setBounds(250, 200, 150, 30);
        textField3.addActionListener(this);
        button1 = new JRoundedButton("Save Advance");
        button1.setBounds(150, 275, 175, 30);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {

                int id = Integer.parseInt(textField1.getText());
                int advance = 0, cattle = 0;

                if (textField1.getText().length() == 0) {
                    JOptionPane.showMessageDialog(frame, "Please Fill Details");
                    return;
                }
                if (textField2.getText().length() == 0) {
                    advance = 0;
                }
                else {
                    advance = Integer.parseInt(textField2.getText());
                }
                if (textField3.getText().length() == 0) {
                    cattle = 0;
                }
                else {
                    cattle = Integer.parseInt(textField3.getText());
                }

                ResultSet resultSet = null;
                try {
                    resultSet = statement.executeQuery("select * from advance where id="+id);
                    while (resultSet.next()) {
                        advance = advance + Integer.parseInt(resultSet.getString(2));
                        cattle = cattle + Integer.parseInt(resultSet.getString(3));
                    }
                } catch (SQLException e1) {
                }

                try {
                    statement.executeUpdate("update advance set advance="+advance+", cattle="+cattle+" where id="+id);
                    statement.executeUpdate("update slipadvance set advance="+advance+", cattle="+cattle+" where id="+id);
                    textField1.setText(null);
                    textField2.setText(null);
                    textField3.setText(null);
                }
                catch (SQLException e1) {
                }
            }
        });

        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(textField1);
        panel.add(textField2);
        panel.add(textField3);
        panel.add(button1);

        setBackground(Color.WHITE);
        setLayout(null);
        add(name);
        add(panel);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == textField1) {
            textField2.requestFocus();
        }
        if (e.getSource() == textField2) {
            textField3.requestFocus();
        }
        if (e.getSource() == textField3) {
            button1.doClick();
            textField1.requestFocus();
        }
    }
}

class PaymentSlip extends JPanel implements ActionListener {

    JTextField textField1, textField2, textField3, textField4;
    JButton button1;
    ArrayList<Integer> customer = new ArrayList<>();
    final String[] startingDate = {null};
    final String[] endingDate = { null };

    PaymentSlip(JFrame frame, Statement statement, int width, int height) {

        final int[] count = {0};

        JLabel name = new TabName("Payment Slip", width/2, height/2-212);

        JPanel panel1 = new JPanel();
        panel1.setBounds(width/2-650, height/2-212, 500, 425);
        panel1.setBackground(Color.BLACK);
        panel1.setLayout(null);

        JLabel label1 = new JRoundedLabel("From");
        label1.setBounds(100, 50, 100, 30);
        textField1 = new JTextField();
        textField1.setBounds(250, 50, 150, 30);
        textField1.addActionListener(this);
        JLabel label2 = new JRoundedLabel("To");
        label2.setBounds(100, 125, 100, 30);
        textField2 = new JTextField();
        textField2.setBounds(250, 125, 150, 30);
        textField2.addActionListener(this);
        JLabel label3 = new JRoundedLabel("Starting Date");
        label3.setBounds(100, 200, 100, 30);
        textField3 = new JTextField();
        textField3.setBounds(250, 200, 150, 30);
        textField3.addActionListener(this);
        JLabel label4 = new JRoundedLabel("Ending Date");
        label4.setBounds(100, 275, 100, 30);
        textField4 = new JTextField();
        textField4.setBounds(250, 275, 150, 30);
        textField4.addActionListener(this);
        button1 = new JRoundedButton("Payment");
        button1.setBounds(150, 350, 175, 30);

        panel1.add(label1);
        panel1.add(textField1);
        panel1.add(label2);
        panel1.add(textField2);
        panel1.add(label3);
        panel1.add(textField3);
        panel1.add(label4);
        panel1.add(textField4);
        panel1.add(button1);

        JPanel panel2 = new JPanel();
        panel2.setBounds(width/2, height/2-325, 600, 650);
        panel2.setBackground(Color.BLACK);
        panel2.setLayout(null);

        JLabel label5 = new JRoundedLabel("Type");
        label5.setBounds(200, 25, 100, 30);
        JTextField textField5 = new JTextField();
        textField5.setBounds(275, 25, 150, 30);
        textField5.setEditable(false);

        int h = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        int v = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
        String column[] = new String[] {"ID","Name","Total Litre","Total Amount","Advance","Cattle Feed","Bill Charge","Net Amount"};
        JTable table = new JTable();
        table.setBackground(Color.WHITE);

        JScrollPane jScrollPane = new JScrollPane(table, v, h);
        jScrollPane.setBounds(0, 75, 600, 400);
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
        defaultTableModel.setColumnIdentifiers(column);
        table.setModel(defaultTableModel);

        JLabel label8 = new JRoundedLabel("Total Liter");
        label8.setBounds(50, 500, 100, 30);
        JTextField textField8 = new JTextField();
        textField8.setBounds(50, 550, 85, 30);
        textField8.setEditable(false);
        JLabel label9 = new JRoundedLabel("Total Amount");
        label9.setBounds(135, 500, 100, 30);
        JTextField textField9 = new JTextField();
        textField9.setBounds(135, 550, 110, 30);
        textField9.setEditable(false);
        JLabel label10 = new JRoundedLabel("Advance");
        label10.setBounds(245, 500, 100, 30);
        JTextField textField10 = new JTextField();

        textField10.setBounds(245, 550, 100, 30);
        textField10.setEditable(false);
        JLabel label11 = new JRoundedLabel("Cattle Feed");
        label11.setBounds(345, 500, 150, 30);
        JTextField textField11 = new JTextField();
        textField11.setBounds(345, 550, 135, 30);
        textField11.setEditable(false);
        JLabel label12 = new JRoundedLabel("Net Pay");
        label12.setBounds(480, 500, 100, 30);
        JTextField textField12 = new JTextField();
        textField12.setBounds(480, 550, 70, 30);
        textField12.setEditable(false);

        JButton button2 = new JRoundedButton("Print");
        button2.setBounds(250, 600, 100, 30);

        panel2.add(label5);
        panel2.add(textField5);
        panel2.add(jScrollPane);
        panel2.add(button2);
        panel2.add(label8);
        panel2.add(textField8);
        panel2.add(label9);
        panel2.add(textField9);
        panel2.add(label10);
        panel2.add(textField10);
        panel2.add(label11);
        panel2.add(textField11);
        panel2.add(label12);
        panel2.add(textField12);

        final int[] from = {0};
        final int[] to = {0};
        startingDate[0] = textField3.getText();
        endingDate[0] = textField4.getText();

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (textField1.getText().length() == 0 || textField2.getText().length() == 0 || textField3.getText().length() == 0 || textField4.getText().length() == 0) {
                    JOptionPane.showMessageDialog(frame, "Please fill details.");
                    return;
                }

                from[0] = Integer.parseInt(textField1.getText());
                to[0] = Integer.parseInt(textField2.getText());
                startingDate[0] = textField3.getText();
                endingDate[0] = textField4.getText();

                count[0] = 0;
                int liter = 0, amount = 0, net = 0, advance = 0, cattle = 0;
                String name = null;
                String type = new checkType().chechType(from[0]);
                textField5.setText(type);
                int totalLiter = 0, totalAmount = 0, totalAdvance = 0, totalCattle = 0, totalNet = 0;

                try {
                    ResultSet resultSet = statement.executeQuery("select id from customer where id between "+ from[0] +" and "+ to[0] +" order by id asc");
                    while (resultSet.next()) {
                        customer.add(resultSet.getInt(1));
                    }
                }
                catch (SQLException se) {
                }
                for (int x:customer) {
                    try {
                        ResultSet resultSet = statement.executeQuery("select sum(r.liter),sum(r.amount) from record r where r.id="+x+" and dt between to_date('"+startingDate[0]+"','dd/mm/yyyy') and to_date('"+endingDate[0]+"','dd/mm/yyyy')");
                        while (resultSet.next()) {
                            liter = resultSet.getInt(1);
                            amount = resultSet.getInt(2);
                            totalLiter += liter;
                            totalAmount += amount;
                        }
                        resultSet = statement.executeQuery("select name from customer where id=" + x);
                        while (resultSet.next()) {
                            name = resultSet.getString(1);
                        }
                        resultSet = statement.executeQuery("select advance, cattle from slipadvance where id=" + x);
                        while (resultSet.next()) {
                            advance = resultSet.getInt(1);
                            cattle = resultSet.getInt(2);
                            totalAdvance += advance;
                            totalCattle += cattle;
                        }
                        net = amount - advance - cattle - 1;
                        totalNet += net;
                    } catch (SQLException e1) {
                    }
                    defaultTableModel.addRow(new Object[] {x,name,liter,amount,advance,cattle,1,net});
                }
                textField8.setText(String.valueOf(totalLiter));
                textField9.setText(String.valueOf(totalAmount));
                textField10.setText(String.valueOf(totalAdvance));
                textField11.setText(String.valueOf(totalCattle));
                textField12.setText(String.valueOf(totalNet));
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveInFile(statement, from[0], to[0], startingDate[0], endingDate[0]);
                } catch (IOException e1) {
                }
            }
        });

        setBackground(Color.WHITE);
        setLayout(null);
        add(name);
        add(panel1);
        add(panel2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == textField1) {
            textField2.requestFocus();
        }
        if (e.getSource() == textField2) {
            textField3.requestFocus();
        }
        if (e.getSource() == textField3) {
            textField4.requestFocus();
        }
        if (e.getSource() == textField4) {
            button1.doClick();
            textField1.requestFocus();
        }
    }

    private void saveInFile(Statement statement, int from, int to, String startingDate, String endingDate) throws IOException {

        FileWriter fileWriter = new FileWriter(new File("dairy.txt"));

        int liter = 0, amount = 0, net = 0, advance = 0, cattle = 0;
        String name = null;
        String type = new checkType().chechType(from);
        int totalLiter = 0, totalAmount = 0, totalAdvance = 0, totalCattle = 0, totalNet = 0;

        fileWriter.append("                                                        PADALKAR ENTERPRISES" + System.lineSeparator());
        fileWriter.append("================================================================================================================================" + System.lineSeparator());
        if (type == "Cow") {
            for (int i = 0; i < 53; i++)
                fileWriter.append(" ");
            fileWriter.append("Cow");
        }
        if (type == "Buffalo") {
            for (int i = 0; i < 51; i++)
                fileWriter.append(" ");
            fileWriter.append("Buffalo");
        }
        fileWriter.append(" Milk Payment Register" + System.lineSeparator());
        fileWriter.append("                                                Period From " + startingDate + " To " + endingDate + System.lineSeparator());
        fileWriter.append("================================================================================================================================" + System.lineSeparator());
        fileWriter.append("================================================================================================================================" + System.lineSeparator());
        fileWriter.append("   ID    Customer Name        Total Litre   Total Amount   Cattle Feed   Advance Amount   Bill Charge   Net Amount   Signature" + System.lineSeparator());
        fileWriter.append("================================================================================================================================" + System.lineSeparator());

        System.out.println(customer);

        for (int x:customer) {
            try {
                ResultSet resultSet = statement.executeQuery("select sum(r.liter),sum(r.amount) from record r where r.id="+x+" and dt between to_date('"+startingDate+"','dd/mm/yyyy') and to_date('"+endingDate+"','dd/mm/yyyy')");
                while (resultSet.next()) {
                    liter = resultSet.getInt(1);
                    amount = resultSet.getInt(2);
                    totalLiter += liter;
                    totalAmount += amount;
                }
                resultSet = statement.executeQuery("select name from customer where id=" + x);
                while (resultSet.next()) {
                    name = resultSet.getString(1);
                }
                resultSet = statement.executeQuery("select advance, cattle from slipadvance where id=" + x);
                while (resultSet.next()) {
                    advance = resultSet.getInt(1);
                    cattle = resultSet.getInt(2);
                    totalAdvance += advance;
                    totalCattle += cattle;
                }
                net = amount - advance - cattle - 1;
                totalNet += net;
                if (net < 0) {
                    statement.executeUpdate("update table slipadvance set advance="+advance+", cattle=0 where id=" + x);
                }
                else {
                    statement.executeUpdate("update table slipadvance set advance=0, cattle=0 where id=" + x);
                }
            } catch (SQLException e1) {
            }
            fileWriter.append("   " + x);
            for (int i = 0; i < 6 - String.valueOf(x).length(); i++)
                fileWriter.append(" ");
            fileWriter.append(name);
            for (int i = 0; i < 21 - name.length(); i++)
                fileWriter.append(" ");
            fileWriter.append(String.valueOf(liter));
            for (int i = 0; i < 14 - String.valueOf(liter).length(); i++)
                fileWriter.append(" ");
            fileWriter.append(String.valueOf(amount));
            for (int i = 0; i < 15 - String.valueOf(amount).length(); i++)
                fileWriter.append(" ");
            fileWriter.append(String.valueOf(advance));
            for (int i = 0; i < 14 - String.valueOf(advance).length(); i++)
                fileWriter.append(" ");
            fileWriter.append(String.valueOf(cattle));
            for (int i = 0; i < 17 - String.valueOf(cattle).length(); i++)
                fileWriter.append(" ");
            fileWriter.append("1");
            for (int i = 0; i < 14 - 1; i++)
                fileWriter.append(" ");
            fileWriter.append(String.valueOf(net));
            for (int i = 0; i < 13 - String.valueOf(net).length(); i++)
                fileWriter.append(" ");
            fileWriter.append("__________" + System.lineSeparator());
        }
        fileWriter.append("================================================================================================================================" + System.lineSeparator());
        fileWriter.append("   Total");
        for (int i = 0; i < 22; i++)
            fileWriter.append(" ");
        fileWriter.append(String.valueOf(totalLiter));
        for (int i = 0; i < 14 - String.valueOf(totalLiter).length(); i++) {
            fileWriter.append(" ");
        }
        fileWriter.append(String.valueOf(totalAmount));
        for (int i = 0; i < 15 - String.valueOf(totalAmount).length(); i++) {
            fileWriter.append(" ");
        }
        fileWriter.append(String.valueOf(totalAdvance));
        for (int i = 0; i < 14 - String.valueOf(totalAdvance).length(); i++) {
            fileWriter.append(" ");
        }
        fileWriter.append(String.valueOf(totalCattle));
        for (int i = 0; i < 31 - String.valueOf(totalCattle).length(); i++) {
            fileWriter.append(" ");
        }
        fileWriter.append(String.valueOf(totalNet) + System.lineSeparator());
        fileWriter.append("================================================================================================================================");
        fileWriter.close();
    }
}

class checkType {


    public String chechType (int id) {
        if (id < 500) {
            return "Cow";
        }
        else {
            return "Buffalo";
        }
    }
}

class JRoundedLabel extends JLabel {

    JRoundedLabel(String text) {

        super(text);
        Font font = new Font("Arial", Font.PLAIN, 15);
        setFont(font);
        setForeground(Color.WHITE);
    }
}

class JRoundedButton extends JButton {

    JRoundedButton(String text) {

        super(text);
        Font font = new Font("Arial", Font.PLAIN, 15);
        setFont(font);
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
    }
}

class JRoundedRadioButton extends JRadioButton {

    JRoundedRadioButton(String text) {

        super(text);
        Font font = new Font("Arial", Font.PLAIN, 15);
        setFont(font);
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
    }
}

class TabName extends JLabel {

    TabName(String name, int width, int height) {

        super(name);
        Font font = new Font("Times New Roman", Font.PLAIN, 50);
        setFont(font);
        setForeground(Color.BLACK);
        setBounds(width/2-300, height/2-30, 600, 60);
        setHorizontalAlignment(CENTER);
    }
}