import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

public class BLM extends JFrame{

    private NameGenerator ng;
    private JLabel label;
    private String filename = "";
    public BLM(){

        JFrame frame = new JFrame();
        frame.setMinimumSize(new Dimension(600, 500));
        JPanel panel = new JPanel();
        JPanel labelPanel = new JPanel();
        JPanel btnPanel = new JPanel();
        JButton nameGen = new JButton("Generate Name");
        nameGen.setSize(150, 75);
        JButton tableGen = new JButton("Get bigram table");
        tableGen.setSize(150, 75);
        JButton chooseButton = new JButton("Choose file:");
        chooseButton.setSize(150, 50);
        label = new JLabel("Choose an action");
        label.setHorizontalAlignment(JLabel.CENTER);

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

        GridLayout btnLayout = new GridLayout(0, 3);
        btnLayout.setHgap(30);
        btnPanel.setLayout(btnLayout);
        btnPanel.add(chooseButton);
        btnPanel.add(nameGen);
        btnPanel.add(tableGen);

        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(BLM.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    filename = chooser.getSelectedFile().getAbsolutePath();
                    try{
                        ng = new NameGenerator(new File(filename));
                    }
                    catch (FileNotFoundException ex){
                        System.out.println("No such file or Directory!");
                    }
                }
            }
        });

        nameGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!filename.equals("")){
                    String name = ng.generateName();
                    label.setText("String generated: " + name);
                }
                else{
                    label.setText("No valid file chosen!");
                }
            }
        });

        tableGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!filename.equals("")) HashMapToJTable(ng.getBigramFreq());
                else label.setText("No valid file chosen!");
            }
        });

        GridLayout labelLayout = new GridLayout(1, 0);
        labelPanel.setLayout(labelLayout);
        labelPanel.add(label);

        GridLayout layout = new GridLayout(2, 0);
        panel.setLayout(layout);
        panel.add(btnPanel);
        panel.add(labelPanel);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Bigram Language Model");
        frame.setVisible(true);
    }

    private void HashMapToJTable(Map<String, Double> map){
        JFrame frame = new JFrame();
        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Bigram:");
        model.addColumn("Probability:");

        for (String key : map.keySet()) {
            model.addRow(new Object[]{key, map.get(key)});
        }

        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new BLM();
    }
}
