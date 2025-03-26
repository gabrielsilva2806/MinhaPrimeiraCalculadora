import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Calculadora extends JFrame implements ActionListener {
    private final JTextField displayExpression;
    private final JTextField displayResult;
    private String operador;
    private double num1, num2, resultado;
    private final StringBuilder expression;

    public Calculadora() {
        setTitle("Calculadora");
        setSize(350, 555);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(Color.BLACK);

        // Expressão
        displayExpression = new JTextField();
        displayExpression.setFont(new Font("Arial", Font.PLAIN, 20));
        displayExpression.setHorizontalAlignment(JTextField.RIGHT);
        displayExpression.setEditable(false);
        displayExpression.setBackground(Color.BLACK);
        displayExpression.setForeground(Color.LIGHT_GRAY);
        displayExpression.setBorder(null);
        displayExpression.setPreferredSize(new Dimension(320, 80));

        // Resultado
        displayResult = new JTextField();
        displayResult.setFont(new Font("Arial", Font.BOLD, 45));
        displayResult.setHorizontalAlignment(JTextField.RIGHT);
        displayResult.setEditable(false);
        displayResult.setBackground(Color.BLACK);
        displayResult.setForeground(Color.WHITE);
        displayResult.setBorder(null);
        displayResult.setPreferredSize(new Dimension(320, 80));

        displayPanel.add(displayExpression, BorderLayout.NORTH);
        displayPanel.add(displayResult, BorderLayout.CENTER);
        add(displayPanel, BorderLayout.NORTH);

        expression = new StringBuilder();

        displayExpression.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if (Character.isDigit(key) || key == KeyEvent.VK_COMMA) {
                    expression.append(key == KeyEvent.VK_COMMA ? "." : key);
                    displayExpression.setText(expression.toString());
                } else if (key == KeyEvent.VK_ENTER) {
                    calcular();
                } else if (key == KeyEvent.VK_BACK_SPACE) {
                    if (expression.length() > 0) {
                        expression.deleteCharAt(expression.length() - 1);
                        displayExpression.setText(expression.toString());
                    }
                } else if ("+-*/".indexOf(key) != -1) {
                    if (expression.length() > 0) {
                        num1 = Double.parseDouble(expression.toString());
                        operador = String.valueOf(key);
                        expression.append(" ").append(operador).append(" ");
                        displayExpression.setText(expression.toString());
                    }
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 2, 2));
        panel.setBackground(Color.BLACK);

        String[] buttons = {
                "C", "%", "√", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                ",", "0", "⌫", "="
        };

        Color black = new Color(0, 0, 0);
        Color green = new Color(0, 250, 0);

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 25));
            button.setBackground(black);
            button.setForeground(Color.white);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            if (text.equals("C")) {
                button.setForeground(Color.RED);
                button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 30));
            }

            if (text.equals("=")) {
                button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 30));
            }

            if (text.equals("-")) {
                button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 35));
            }

            if (text.equals(",")) {
                button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
            }

            if ("%√/*-+=⌫".contains(text)) {
                button.setForeground(green);
            }

            button.addActionListener(this);
            panel.add(button);
        }

        add(panel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private static JButton getJButton(String text) {
        return new JButton(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]") || command.equals(",")) {
            expression.append(command.equals(",") ? "." : command);
            displayExpression.setText(expression.toString());

        } else if (command.matches("[/*\\-+]") && expression.length() > 0) {
            num1 = Double.parseDouble(expression.toString());
            operador = command;
            expression.append(" ").append(operador).append(" ");
            displayExpression.setText(expression.toString());

        } else if (command.equals("=")) {
            calcular();

        } else if (command.equals("C")) {
            expression.setLength(0);
            num1 = num2 = resultado = 0;
            displayExpression.setText("");
            displayResult.setText("");

        } else if (command.equals("√")) {
            if (expression.length() > 0) {
                num1 = Double.parseDouble(expression.toString());
                resultado = Math.sqrt(num1);
                displayExpression.setText("√" + num1);
                displayResult.setText(String.valueOf(resultado));
                expression.setLength(0);
            }

        } else if (command.equals("%")) {
            if (expression.length() > 0) {
                // Dividir a expressão para obter o primeiro número (num1)
                String[] partes = expression.toString().split(" ");
                num1 = Double.parseDouble(partes[0]);

                // Se a operação for de porcentagem, o segundo número será num2
                if (partes.length > 1) {
                    num2 = Double.parseDouble(partes[2]);  // O segundo número após o operador
                }

                // Calcular a porcentagem corretamente dependendo do operador
                if (operador.equals("+")) {
                    // Porcentagem adicionada ao primeiro número
                    resultado = num1 + (num1 * num2 / 100);
                } else if (operador.equals("-")) {
                    // Porcentagem subtraída do primeiro número
                    resultado = num1 - (num1 * num2 / 100);
                }

                displayExpression.setText(expression + " %");
                displayResult.setText(String.valueOf(resultado));
                expression.setLength(0); // Limpa a expressão após a operação
            }

        } else if (command.equals("⌫")) {
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 1);
                displayExpression.setText(expression.toString());
            }
        }
    }

    private void calcular() {
        if (expression.length() > 0) {
            num2 = Double.parseDouble(expression.toString().split(" ")[2]);
            switch (operador) {
                case "+" -> resultado = num1 + num2;
                case "-" -> resultado = num1 - num2;
                case "*" -> resultado = num1 * num2;
                case "/" -> resultado = num1 / num2;
            }
            displayExpression.setText(expression.toString());
            displayResult.setText(String.valueOf(resultado));
            expression.setLength(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculadora::new);
    }
}
