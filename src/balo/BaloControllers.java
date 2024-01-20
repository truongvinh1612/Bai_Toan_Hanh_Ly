package balo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

class DoVat {
    String TenDV;
    float TL, GT, DG;
    int PA;

    public DoVat(String tenDV, float tl, float gt) {
        TenDV = tenDV;
        TL = tl;
        GT = gt;
        DG = GT / TL;
        PA = 0;
    }
}

public class BaloControllers extends JFrame {
    private Balo balo;

    private JLabel weightLabel;
    private JTextField weightTextField;
    private JButton calculateButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel totalValueLabel;
    private JLabel totalWeightLabel;
    private JComboBox<String> greedyComboBox;
    private JButton fileOutputButton;
    private JButton readFileButton;
    private JButton calculateButton2;
    private JButton removeButton;
    private DoVat[] dsdv;

    public BaloControllers() {
        setTitle("ĐIỀU TRA THÔNG TIN HÀNH LÝ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(850, 400);
        setLocationRelativeTo(null);

        weightLabel = new JLabel("Trọng Lượng BaLo:");
        weightTextField = new JTextField(10);
        calculateButton = new JButton("Nhập Dữ Liệu");
        readFileButton = new JButton("Đọc Dữ Liệu");
        resultTable = new JTable();
        tableModel = new DefaultTableModel(
                new Object[]{"STT", "Tên Đồ Vật", "Trọng Lượng", "Giá Trị", "Đơn Giá", "Số Lượng"}, 0);
        resultTable.setModel(tableModel);
        totalValueLabel = new JLabel();
        totalWeightLabel = new JLabel();

        String[] greedyOptions = {"Lấy đồ vật phù hợp với Giá Trị lớn", "Lấy đồ vật phù hợp với Đơn Giá lớn", "Lấy đồ vật phù hợp với Trọng Lượng nhỏ", "Lấy mỗi đồ vật tối đa một lần"};
        greedyComboBox = new JComboBox<>(greedyOptions);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(weightLabel);
        inputPanel.add(weightTextField);
        inputPanel.add(greedyComboBox);
        inputPanel.add(calculateButton);
        inputPanel.add(readFileButton);

        JScrollPane scrollPane = new JScrollPane(resultTable);

        JPanel navBarPanel = new JPanel();
        navBarPanel.setLayout(new BoxLayout(navBarPanel, BoxLayout.X_AXIS));
        navBarPanel.add(totalValueLabel);
        navBarPanel.add(totalWeightLabel);
        navBarPanel.add(Box.createHorizontalGlue());
        fileOutputButton = new JButton("Xuất File");
        calculateButton2 = new JButton("Tính Toán");
        removeButton = new JButton("Xoá");
        navBarPanel.add(removeButton);
        navBarPanel.add(calculateButton2);
        navBarPanel.add(fileOutputButton);
        

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navBarPanel, BorderLayout.SOUTH);

        calculateButton.addActionListener((ActionEvent e) -> {
            tableModel.setRowCount(0);

            String weightText = weightTextField.getText();
            if (weightText.isEmpty()) {
                JOptionPane.showMessageDialog(BaloControllers.this, "Vui lòng nhập trọng lượng ba lô.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            float maxWeight;
            try {
                maxWeight = Float.parseFloat(weightText);
                if (maxWeight <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BaloControllers.this, "Trọng lượng ba lô không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int numItems = 0;
            boolean isValidNumItems = false;
            do {
                try {
                    String input = JOptionPane.showInputDialog("Số lượng đồ vật:");
                    if (input == null) {
                        System.exit(0);
                    }
                    numItems = Integer.parseInt(input);
                    if (numItems <= 0) {
                        throw new NumberFormatException();
                    }
                    isValidNumItems = true;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BaloControllers.this, "Số lượng đồ vật không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } while (!isValidNumItems);


            dsdv = new DoVat[numItems];

            for (int i = 0; i < numItems; i++) {
                String tenDV;
                float tl = 0, gt = 0;
                boolean invalidInput = true;

                do {
                    tenDV = JOptionPane.showInputDialog("Tên Đồ Vật " + (i + 1) + ":");
                    try {
                        tl = Float.parseFloat(JOptionPane.showInputDialog("Trọng Lượng " + (i + 1) + ":"));
                        gt = Float.parseFloat(JOptionPane.showInputDialog("Giá Trị " + (i + 1) + ":"));

                        if (tl <= 0 || gt <= 0) {
                            throw new NumberFormatException();
                        }

                        invalidInput = false; // Đánh dấu giá trị là hợp lệ để thoát khỏi vòng lặp
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(BaloControllers.this, "Giá trị hoặc trọng lượng đồ vật không hợp lệ. Vui lòng nhập lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } while (invalidInput);

                dsdv[i] = new DoVat(tenDV, tl, gt);
            }

            balo = new Balo(maxWeight);
            balo.setDoVat(dsdv);

            calculateButton2.setEnabled(true);

            JOptionPane.showMessageDialog(BaloControllers.this, "Dữ liệu đã được nhập thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            for (int i = 0; i < dsdv.length; i++) {
                DoVat dv = dsdv[i];
                tableModel.addRow(new Object[]{
                    i + 1, dv.TenDV, dv.TL, dv.GT, dv.DG, dv.PA
                });
            }
        });
        

        
        calculateButton2.addActionListener((ActionEvent e) -> {
            String selectedGreedyOption = (String) greedyComboBox.getSelectedItem();
            switch (selectedGreedyOption) {
                case "Lấy đồ vật phù hợp với Giá Trị lớn" -> {
                    balo.SortByValue();
                    balo.greedy();
                    break;
                }
                case "Lấy đồ vật phù hợp với Trọng Lượng nhỏ" -> {
                    balo.SortByWeight();
                    balo.greedy();
                    break;
                }
                case "Lấy đồ vật phù hợp với Đơn Giá lớn" -> {
                    balo.SortByPrice();
                    balo.greedy();
                    break;
                }
                case "Lấy mỗi đồ vật tối đa một lần" -> {
                    balo.greedyByMaxOnce();
                    break;
                }
            }
            
            float totalWeight = 0;
            float totalValue = 0;
            
            tableModel.setRowCount(0);
            
            for (int i = 0; i < dsdv.length; i++) {
                DoVat dv = dsdv[i];
                tableModel.addRow(new Object[]{
                    i + 1, dv.TenDV, dv.TL, dv.GT, dv.DG, dv.PA
                });
                totalWeight += dv.PA * dv.TL;
                totalValue += dv.PA * dv.GT;
            }
            
            totalValueLabel.setText("Tổng Giá Trị: " + totalValue + "        ");
            totalValueLabel.setFont(totalWeightLabel.getFont().deriveFont(Font.BOLD));
            totalWeightLabel.setText("Tổng Trọng Lượng: " + totalWeight);
            totalWeightLabel.setFont(totalWeightLabel.getFont().deriveFont(Font.BOLD));
        });

        
        fileOutputButton.addActionListener((ActionEvent e) -> {
            writeFileOutput();
        });
        readFileButton.addActionListener((ActionEvent e) -> {
            readDataFromFile();
        });
        
        removeButton.addActionListener((ActionEvent e) -> {
            int rowCount = tableModel.getRowCount();
            
            weightTextField.setText("");
            for (int i = rowCount - 1; i >= 0; i--) {
                tableModel.removeRow(i);
            }
            totalValueLabel.setText("");
            totalWeightLabel.setText("");
        });
        
    }
    
     private boolean readDataFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(BaloControllers.this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isReadingData = false;
                boolean isValidData = true;
                ArrayList<DoVat> temporaryItemList = new ArrayList<>();
                String weightText = "";

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Trọng Lượng BaLo:")) {
                        weightText = line.split(": ")[1];
                    } else if (line.startsWith("Tên Đồ Vật") && !line.equals("Tên Đồ Vật")) {
                        isReadingData = true;
                        String tenDV = line.split(": ")[1];
                        float tl = 0, gt = 0;

                        try {
                            line = reader.readLine();
                            tl = Float.parseFloat(line.split(": ")[1]);

                            line = reader.readLine(); 
                            gt = Float.parseFloat(line.split(": ")[1]);

                            reader.readLine();

                            reader.readLine();

                            temporaryItemList.add(new DoVat(tenDV, tl, gt));
                        } catch (NumberFormatException ex) {
                            isValidData = false;
                            break;
                        }
                    } else if (isReadingData && line.isEmpty()) {
                        isReadingData = false;
                    }
                }

                if (!isValidData) {
                    JOptionPane.showMessageDialog(BaloControllers.this, "Dữ liệu không hợp lệ trong file.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                dsdv = new DoVat[temporaryItemList.size()];
                temporaryItemList.toArray(dsdv);
                balo = new Balo(Float.parseFloat(weightText));
                balo.setDoVat(dsdv);
                calculateButton2.setEnabled(true);

                tableModel.setRowCount(0);
                weightTextField.setText(weightText);
                for (int i = 0; i < dsdv.length; i++) {
                    DoVat dv = dsdv[i];
                    tableModel.addRow(new Object[]{
                        i + 1, dv.TenDV, dv.TL, dv.GT, dv.PA, dv.PA * dv.GT
                    });
                }

                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(BaloControllers.this, "Lỗi khi đọc file.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return false;
    }

    private void writeFileOutput() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(BaloControllers.this);
        String weightText = weightTextField.getText();
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Trọng Lượng BaLo: " + weightText);
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.println("");
                    writer.println("Tên Đồ Vật " + (i+1) + ": "  + tableModel.getValueAt(i, 1));
                    writer.println("Trọng Lượng " + (i+1) + ": " + tableModel.getValueAt(i, 2));
                    writer.println("Giá Trị " + (i+1) + ": " + tableModel.getValueAt(i, 3));
                    writer.println("Đơn Giá " + (i+1) + ": " + tableModel.getValueAt(i, 4));
                    writer.println("Số Lượng " + (i+1) + ": " + tableModel.getValueAt(i, 5));
                }

                JOptionPane.showMessageDialog(BaloControllers.this, "Dữ liệu đã được ghi vào file thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(BaloControllers.this, "Đã xảy ra lỗi khi ghi file.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BaloControllers baloControllers = new BaloControllers();
            baloControllers.setVisible(true);
        });
    }
}
