import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * ╔══════════════════════════════════════════════════════╗
 * ║         BINARY CALCULATOR  –  BIN-CALC v1.0         ║
 * ║  Base-2 Arithmetic & Bitwise Logic Engine            ║
 * ║                                                      ║
 * ║  Supported operations:                               ║
 * ║   Arithmetic  :  +  −  ×  ÷                         ║
 * ║   Bitwise     :  AND  OR  XOR  NOT                   ║
 * ║                  NAND  NOR  XNOR                     ║
 * ║   Shift       :  <<  >>                              ║
 * ║                                                      ║
 * ║  Input        :  binary digits only  (0 and 1)       ║
 * ║  Display      :  binary result + decimal equivalent  ║
 * ║  Max width    :  32 bits                             ║
 * ╚══════════════════════════════════════════════════════╝
 *
 * @author  Mateus
 * @version 1.0
 */
public class BinaryCalculator extends JFrame {

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final Color BG_DARK      = new Color(10,  12,  18);
    private static final Color BG_PANEL     = new Color(18,  22,  34);
    private static final Color BG_DISPLAY   = new Color(8,   10,  16);
    private static final Color ACCENT_GREEN = new Color(0,   255, 140);
    private static final Color ACCENT_CYAN  = new Color(0,   210, 255);
    private static final Color BTN_NUM      = new Color(24,  30,  48);
    private static final Color BTN_OP       = new Color(16,  38,  56);
    private static final Color BTN_BITWISE  = new Color(28,  18,  52);
    private static final Color BTN_SPECIAL  = new Color(50,  18,  18);
    private static final Color BTN_EQUALS   = new Color(0,   115, 72);
    private static final Color TEXT_PRIMARY = new Color(210, 230, 255);
    private static final Color TEXT_DIM     = new Color(80,  100, 130);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    private static final Font F_DISPLAY = new Font("Courier New", Font.BOLD,  30);
    private static final Font F_DECIMAL = new Font("Courier New", Font.PLAIN, 13);
    private static final Font F_LABEL   = new Font("Courier New", Font.BOLD,  10);
    private static final Font F_BTN     = new Font("Courier New", Font.BOLD,  15);
    private static final Font F_BTN_SM  = new Font("Courier New", Font.BOLD,  12);
    private static final Font F_HIST    = new Font("Courier New", Font.PLAIN, 11);

    // ── State ─────────────────────────────────────────────────────────────────
    private String  currentInput = "0";
    private String  firstOperand = null;
    private String  operator     = null;
    private boolean newEntry     = true;
    private boolean errorState   = false;

    // ── UI Widgets ────────────────────────────────────────────────────────────
    private JLabel    displayLabel;
    private JLabel    decimalLabel;
    private JLabel    operatorLabel;
    private JLabel    firstOpLabel;
    private JTextArea historyArea;

    // =========================================================================
    public BinaryCalculator() {
        super("Binary Calculator  //  BIN-CALC v1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        root.add(makeHeader(),  BorderLayout.NORTH);
        root.add(makeLeft(),    BorderLayout.CENTER);
        root.add(makeHistory(), BorderLayout.EAST);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // =========================================================================
    // UI – Header
    // =========================================================================
    private JPanel makeHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(0, 0, 4, 0));

        JLabel title = new JLabel("⬛  BINARY CALCULATOR");
        title.setFont(new Font("Courier New", Font.BOLD, 14));
        title.setForeground(ACCENT_GREEN);

        JLabel sub = new JLabel("Base-2 Arithmetic & Bitwise Logic Engine");
        sub.setFont(new Font("Courier New", Font.PLAIN, 10));
        sub.setForeground(TEXT_DIM);

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setBackground(BG_DARK);
        left.add(title);
        left.add(sub);
        p.add(left, BorderLayout.WEST);
        return p;
    }

    // =========================================================================
    // UI – Left column  (display + buttons)
    // =========================================================================
    private JPanel makeLeft() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG_DARK);
        p.add(makeDisplay(), BorderLayout.NORTH);
        p.add(makeButtons(), BorderLayout.CENTER);
        return p;
    }

    // ── Display ───────────────────────────────────────────────────────────────
    private JPanel makeDisplay() {
        JPanel card = new RoundCard(BG_PANEL, 14);
        card.setLayout(new BorderLayout(0, 2));
        card.setBorder(new EmptyBorder(14, 18, 14, 18));

        // pending operand + operator hint
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(BG_PANEL);

        firstOpLabel = new JLabel(" ");
        firstOpLabel.setFont(F_HIST);
        firstOpLabel.setForeground(TEXT_DIM);

        operatorLabel = new JLabel(" ");
        operatorLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        operatorLabel.setForeground(ACCENT_CYAN);
        operatorLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topRow.add(firstOpLabel,  BorderLayout.WEST);
        topRow.add(operatorLabel, BorderLayout.EAST);

        // main binary display
        displayLabel = new JLabel("0", SwingConstants.RIGHT);
        displayLabel.setFont(F_DISPLAY);
        displayLabel.setForeground(TEXT_PRIMARY);
        displayLabel.setBorder(new EmptyBorder(6, 0, 6, 0));

        // decimal readout
        JPanel decRow = new JPanel(new BorderLayout());
        decRow.setBackground(BG_PANEL);
        decRow.setBorder(new EmptyBorder(4, 0, 0, 0));

        JLabel decTag = new JLabel("DEC  ");
        decTag.setFont(F_LABEL);
        decTag.setForeground(TEXT_DIM);

        decimalLabel = new JLabel("0", SwingConstants.RIGHT);
        decimalLabel.setFont(F_DECIMAL);
        decimalLabel.setForeground(ACCENT_GREEN);

        decRow.add(decTag,       BorderLayout.WEST);
        decRow.add(decimalLabel, BorderLayout.EAST);

        card.add(topRow,       BorderLayout.NORTH);
        card.add(displayLabel, BorderLayout.CENTER);
        card.add(decRow,       BorderLayout.SOUTH);
        return card;
    }

    // ── Button grid ───────────────────────────────────────────────────────────
    private JPanel makeButtons() {
        JPanel g = new JPanel(new GridLayout(7, 4, 6, 6));
        g.setBackground(BG_DARK);

        // Row 0 – special
        g.add(btn("CLR",  BTN_SPECIAL, TEXT_PRIMARY, F_BTN_SM, e -> clearAll()));
        g.add(btn("CE",   BTN_SPECIAL, TEXT_PRIMARY, F_BTN_SM, e -> clearEntry()));
        g.add(btn("⌫",   BTN_SPECIAL, TEXT_PRIMARY, F_BTN,    e -> backspace()));
        g.add(btn("NOT",  BTN_BITWISE, ACCENT_CYAN,  F_BTN_SM, e -> applyNot()));

        // Row 1 – bitwise A
        g.add(btn("AND",  BTN_BITWISE, ACCENT_CYAN, F_BTN_SM, e -> setOp("AND")));
        g.add(btn("OR",   BTN_BITWISE, ACCENT_CYAN, F_BTN_SM, e -> setOp("OR")));
        g.add(btn("XOR",  BTN_BITWISE, ACCENT_CYAN, F_BTN_SM, e -> setOp("XOR")));
        g.add(btn("XNOR", BTN_BITWISE, ACCENT_CYAN, F_BTN_SM, e -> setOp("XNOR")));

        // Row 2 – shifts + bitwise B
        g.add(btn("<<",   BTN_OP,      ACCENT_CYAN, F_BTN,    e -> setOp("<<")));
        g.add(btn(">>",   BTN_OP,      ACCENT_CYAN, F_BTN,    e -> setOp(">>")));
        g.add(btn("NAND", BTN_BITWISE, ACCENT_CYAN, F_BTN_SM, e -> setOp("NAND")));
        g.add(btn("NOR",  BTN_BITWISE, ACCENT_CYAN, F_BTN_SM, e -> setOp("NOR")));

        // Row 3-6 – digit pad (only 0 and 1 exist in binary)
        // Layout mirrors a standard numpad but only uses 0/1 bits
        g.add(dBtn("1")); g.add(dBtn("1")); g.add(dBtn("0"));
        g.add(btn("÷",  BTN_OP, ACCENT_CYAN,  F_BTN, e -> setOp("/")));

        g.add(dBtn("1")); g.add(dBtn("0")); g.add(dBtn("0"));
        g.add(btn("×",  BTN_OP, ACCENT_CYAN,  F_BTN, e -> setOp("*")));

        g.add(dBtn("1")); g.add(dBtn("0")); g.add(dBtn("1"));
        g.add(btn("−",  BTN_OP, ACCENT_CYAN,  F_BTN, e -> setOp("-")));

        g.add(dBtn("0")); g.add(dBtn("0"));
        g.add(btn("+",  BTN_OP,     ACCENT_CYAN,  F_BTN, e -> setOp("+")));
        g.add(btn("=",  BTN_EQUALS, TEXT_PRIMARY, F_BTN, e -> calculate()));

        return g;
    }

    // =========================================================================
    // UI – History panel
    // =========================================================================
    private JPanel makeHistory() {
        JPanel card = new RoundCard(BG_PANEL, 14);
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new EmptyBorder(14, 12, 14, 12));
        card.setPreferredSize(new Dimension(182, 0));

        JLabel title = new JLabel("HISTORY");
        title.setFont(F_LABEL);
        title.setForeground(ACCENT_GREEN);
        title.setBorder(new EmptyBorder(0, 0, 6, 0));

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setBackground(BG_DISPLAY);
        historyArea.setForeground(new Color(130, 200, 155));
        historyArea.setFont(F_HIST);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(false);
        historyArea.setBorder(new EmptyBorder(6, 6, 6, 6));

        JScrollPane scroll = new JScrollPane(historyArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_DISPLAY);

        JButton clrHist = btn("CLR HIST",
                new Color(35, 16, 16), new Color(190, 70, 70), F_LABEL,
                e -> historyArea.setText(""));
        clrHist.setPreferredSize(new Dimension(0, 26));

        card.add(title,   BorderLayout.NORTH);
        card.add(scroll,  BorderLayout.CENTER);
        card.add(clrHist, BorderLayout.SOUTH);
        return card;
    }

    // =========================================================================
    // Button factories
    // =========================================================================
    private JButton dBtn(String label) {
        return btn(label, BTN_NUM, TEXT_PRIMARY, F_BTN, e -> appendDigit(label));
    }

    private JButton btn(String text, Color bg, Color fg, Font font, ActionListener al) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = getModel().isPressed()  ? bg.brighter().brighter()
                           : getModel().isRollover() ? bg.brighter()
                           : bg;
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // subtle top-edge sheen
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), 2, 3, 3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(font);
        b.setForeground(fg);
        b.setBackground(bg);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(74, 52));
        b.addActionListener(al);
        return b;
    }

    // =========================================================================
    // Calculator Logic
    // =========================================================================

    /** Append a binary digit (0 or 1) to the current input. */
    private void appendDigit(String d) {
        if (errorState) clearAll();
        if (newEntry)   { currentInput = "0"; newEntry = false; }
        currentInput = currentInput.equals("0") ? d : currentInput + d;
        if (currentInput.length() > 32)
            currentInput = currentInput.substring(currentInput.length() - 32);
        refreshDisplay();
    }

    /** Delete the rightmost digit. */
    private void backspace() {
        if (errorState) { clearAll(); return; }
        currentInput = currentInput.length() <= 1
                     ? "0"
                     : currentInput.substring(0, currentInput.length() - 1);
        refreshDisplay();
    }

    /** Clear only the current entry, keeping the stored operand. */
    private void clearEntry() {
        currentInput = "0";
        errorState   = false;
        refreshDisplay();
    }

    /** Full calculator reset. */
    private void clearAll() {
        currentInput = "0";
        firstOperand = null;
        operator     = null;
        newEntry     = true;
        errorState   = false;
        firstOpLabel.setText(" ");
        operatorLabel.setText(" ");
        refreshDisplay();
    }

    /**
     * Store the current value as the first operand and record the operator.
     * If an expression is already pending, it is evaluated first (chaining).
     */
    private void setOp(String op) {
        if (errorState) return;
        if (firstOperand != null && !newEntry) calculate();   // chain evaluation
        if (!errorState) {
            firstOperand = currentInput;
            operator     = op;
            newEntry     = true;
            firstOpLabel.setText(firstOperand);
            operatorLabel.setText("  " + op + "  ");
        }
    }

    /**
     * Bitwise NOT: flips every bit within the current input's width.
     * Example: NOT 1010 (4-bit) → 0101
     */
    private void applyNot() {
        if (errorState) return;
        try {
            long val    = Long.parseUnsignedLong(currentInput, 2);
            int  bits   = currentInput.length();
            long mask   = bits < 64 ? (1L << bits) - 1L : -1L;
            long result = (~val) & mask;
            String binRes = result == 0 ? "0" : Long.toBinaryString(result);
            addHistory(currentInput + "  NOT  →  " + binRes
                     + "  [dec:" + result + "]");
            currentInput = binRes;
            newEntry     = true;
            refreshDisplay();
        } catch (NumberFormatException ex) {
            showError("ERROR");
        }
    }

    /**
     * Evaluate the pending binary expression and update the display.
     */
    private void calculate() {
        if (operator == null || firstOperand == null || errorState) return;

        String second = currentInput;
        try {
            long a  = Long.parseUnsignedLong(firstOperand, 2);
            long b  = Long.parseUnsignedLong(second,       2);
            int  w  = Math.max(firstOperand.length(), second.length());
            long wm = w < 64 ? (1L << w) - 1L : -1L;
            long result;

            switch (operator) {
                case "+"    -> result = a + b;
                case "-"    -> {
                    if (b > a) { showError("NEGATIVE"); return; }
                    result = a - b;
                }
                case "*"    -> result = a * b;
                case "/"    -> {
                    if (b == 0) { showError("DIV / 0"); return; }
                    result = a / b;
                }
                case "AND"  -> result = a & b;
                case "OR"   -> result = a | b;
                case "XOR"  -> result = a ^ b;
                case "XNOR" -> result = (~(a ^ b)) & wm;
                case "NAND" -> result = (~(a & b)) & wm;
                case "NOR"  -> result = (~(a | b)) & wm;
                case "<<"   -> {
                    if (b > 63) { showError("OVERFLOW"); return; }
                    result = (a << b) & 0xFFFFFFFFL;   // keep 32-bit range
                }
                case ">>"   -> result = a >> b;
                default     -> { showError("ERROR"); return; }
            }

            if (result < 0) { showError("OVERFLOW"); return; }

            String binRes = result == 0 ? "0" : Long.toBinaryString(result);
            addHistory(firstOperand + " " + operator + " " + second
                     + " = " + binRes + "  [dec:" + result + "]");

            currentInput = binRes;
            firstOperand = null;
            operator     = null;
            newEntry     = true;
            firstOpLabel.setText(" ");
            operatorLabel.setText(" ");
            refreshDisplay();

        } catch (NumberFormatException ex) {
            showError("ERROR");
        }
    }

    // =========================================================================
    // Display helpers
    // =========================================================================

    private void refreshDisplay() {
        displayLabel.setText(formatBinary(currentInput));
        if (!errorState) {
            try {
                long dec = Long.parseUnsignedLong(currentInput, 2);
                decimalLabel.setText(String.valueOf(dec));
                decimalLabel.setForeground(ACCENT_GREEN);
            } catch (NumberFormatException ignored) {
                decimalLabel.setText("---");
            }
        }
    }

    /**
     * Insert a space every 4 bits for readability.
     * "11001010"  →  "1100 1010"
     */
    private String formatBinary(String bin) {
        if (bin.length() <= 4) return bin;
        StringBuilder sb  = new StringBuilder();
        int           rem = bin.length() % 4;
        if (rem > 0) sb.append(bin, 0, rem);
        for (int i = rem; i < bin.length(); i += 4) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(bin, i, i + 4);
        }
        return sb.toString();
    }

    private void showError(String msg) {
        errorState = true;
        displayLabel.setText(msg);
        decimalLabel.setText("---");
        decimalLabel.setForeground(new Color(255, 75, 75));
    }

    private void addHistory(String entry) {
        historyArea.append(entry + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    // =========================================================================
    // Rounded card panel
    // =========================================================================
    private static class RoundCard extends JPanel {
        private final int r;
        RoundCard(Color bg, int radius) {
            this.r = radius;
            setBackground(bg);
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r * 2, r * 2);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================================
    // Entry point
    // =========================================================================
    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(BinaryCalculator::new);
    }
}
