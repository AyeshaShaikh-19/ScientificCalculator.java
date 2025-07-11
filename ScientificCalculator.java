import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ScientificCalculator extends JFrame implements ActionListener {
    private final JTextField display;
    private final JTextArea historyArea;
    private final StringBuilder input = new StringBuilder();
    private final ArrayList<String> history = new ArrayList<>();
    private boolean isDarkMode = false;
    private JPanel panel;

    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        display = new JTextField();
        display.setFont(new Font("Segoe UI", Font.BOLD, 30));
        display.setEditable(false);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(display, BorderLayout.NORTH);

        historyArea = new JTextArea(5, 20);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        historyArea.setBorder(BorderFactory.createTitledBorder("History"));
        add(new JScrollPane(historyArea), BorderLayout.SOUTH);

        panel = new JPanel(new GridLayout(7, 4, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "sin", "cos", "tan", "sqrt",
                "log", "ln", "^", "C",
                "Toggle", "Save", "(", ")"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 20));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBackground(new Color(200, 200, 255));
            button.addActionListener(this);
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);
        applyTheme();
    }

    private void applyTheme() {
        Color bg = isDarkMode ? new Color(40, 40, 40) : Color.WHITE;
        Color fg = isDarkMode ? Color.WHITE : Color.BLACK;
        Color btnBg = isDarkMode ? new Color(70, 70, 70) : new Color(200, 200, 255);

        panel.setBackground(bg);
        display.setBackground(bg);
        display.setForeground(fg);
        historyArea.setBackground(bg);
        historyArea.setForeground(fg);

        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton btn) {
                btn.setBackground(btnBg);
                btn.setForeground(fg);
            }
        }
    }

    private void saveHistoryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("calculator_history.txt"))) {
            for (String line : history) {
                writer.write(line);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "History saved to calculator_history.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving history");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "=":
                try {
                    String expression = input.toString();
                    Expression exp = new ExpressionBuilder(expression).build();
                    double result = exp.evaluate();
                    String res = expression + " = " + result;
                    display.setText(String.valueOf(result));
                    history.add(res);
                    historyArea.append(res + "\n");
                    input.setLength(0);
                } catch (Exception ex) {
                    display.setText("Error");
                    input.setLength(0);
                }
                break;
            case "C":
                input.setLength(0);
                display.setText("");
                break;
            case "Toggle":
                isDarkMode = !isDarkMode;
                applyTheme();
                break;
            case "Save":
                saveHistoryToFile();
                break;
            default:
                input.append(command);
                display.setText(input.toString());
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScientificCalculator calc = new ScientificCalculator();
            calc.setVisible(true);
        });
    }
}
