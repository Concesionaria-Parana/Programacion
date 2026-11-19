package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PantallaArcade extends JFrame {
    private static final String CARD_BIENVENIDO = "BIENVENIDO";
    private static final String CARD_SISTEMA = "SISTEMA";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedorPrincipal = new JPanel(cardLayout);
    private final TexturePaint texturaFondoArcade;

    private boolean parpadeo = true;
    private JTextArea areaChatBot;
    private JTextField campoEntrada;

    public PantallaArcade() {
        setTitle("CONCESIONARIA '93 - CAPCOM ARCADE SYSTEM");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra limpio toda la app al cerrar la ventana
        setResizable(false);

        this.texturaFondoArcade = crearTextura16BitGranulada();

        PanelBienvenido panelBienvenido = new PanelBienvenido();
        PanelSistema panelSistema = new PanelSistema();

        contenedorPrincipal.add(panelBienvenido, CARD_BIENVENIDO);
        contenedorPrincipal.add(panelSistema, CARD_SISTEMA);

        setContentPane(contenedorPrincipal);

        Timer timer = new Timer(450, e -> {
            parpadeo = !parpadeo;
            panelBienvenido.repaint();
        });
        timer.start();

        // Atajos de teclado en la pantalla de bienvenida
        panelBienvenido.setFocusable(true);
        panelBienvenido.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ingresarAlSistema();
                } else if (e.getKeyCode() == KeyEvent.VK_N || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    salirDelSistema();
                }
            }
        });
    }

    private void ingresarAlSistema() {
        ConsolaUI.sonidoMonedaMario();
        cardLayout.show(contenedorPrincipal, CARD_SISTEMA);
        SwingUtilities.invokeLater(() -> campoEntrada.requestFocusInWindow());
    }

    private void salirDelSistema() {
        ConsolaUI.sonidoError8Bit();
        try {
            Thread.sleep(280);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        dispose();
        System.exit(0);
    }

    private JButton crearBotonArcade(String texto, Color fondo, Color textoColor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Consolas", Font.BOLD, 18));
        btn.setBackground(fondo);
        btn.setForeground(textoColor);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 225, 70), 3),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        return btn;
    }

    private TexturePaint crearTextura16BitGranulada() {
        int bloque = 3;
        int anchoTiles = 64;
        int altoTiles = 64;
        BufferedImage img = new BufferedImage(anchoTiles * bloque, altoTiles * bloque, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        Random rnd = new Random(1993);

        Color[] paletaTextura = {
            new Color(22, 88, 18),
            new Color(30, 110, 22),
            new Color(42, 125, 28),
            new Color(56, 108, 32),
            new Color(74, 92, 34),
            new Color(20, 72, 16)
        };

        for (int ty = 0; ty < altoTiles; ty++) {
            double onda = Math.sin(ty * 0.35) * 2.0 + Math.cos(ty * 0.14);
            for (int tx = 0; tx < anchoTiles; tx++) {
                double patronVeta = Math.sin((tx + onda * 3.0) * 0.42 + Math.cos(ty * 0.5) * 1.6);
                int idx = (int) Math.abs((patronVeta + 1.5) * 1.7 + rnd.nextInt(3)) % paletaTextura.length;
                Color base = paletaTextura[idx];
                if (ty % 2 == 1) {
                    base = base.darker();
                }
                g.setColor(base);
                g.fillRect(tx * bloque, ty * bloque, bloque, bloque);
            }
        }
        g.dispose();
        return new TexturePaint(img, new Rectangle(0, 0, img.getWidth(), img.getHeight()));
    }

    // ==================== PANTALLA 1: BIENVENIDO ====================
    private class PanelBienvenido extends JPanel {
        public PanelBienvenido() {
            setLayout(null);

            JButton btnSi = crearBotonArcade("SI [ENTER]", new Color(45, 210, 85), Color.BLACK);
            btnSi.setBounds(255, 455, 175, 44);
            btnSi.addActionListener(e -> ingresarAlSistema());
            add(btnSi);

            JButton btnNo = crearBotonArcade("NO [ESC]", new Color(235, 50, 40), Color.WHITE);
            btnNo.setBounds(470, 455, 175, 44);
            btnNo.addActionListener(e -> salirDelSistema());
            add(btnNo);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            dibujarFondoConMarco(g2, w, h);

            // HUD Superior
            g2.setFont(new Font("Consolas", Font.BOLD, 20));
            dibujarTextoConSombra(g2, "PLAYER 1  000000", 35, 44, new Color(255, 230, 0), Color.BLACK);
            dibujarTextoConSombra(g2, "TOP SCORE  019300", 335, 44, new Color(255, 90, 210), Color.BLACK);
            dibujarTextoConSombra(g2, "UTN - PROG II", 685, 44, Color.WHITE, Color.BLACK);

            // Título "Bienvenido" en cursiva manuscrita
            Font fuenteCursiva = obtenerFuenteCursiva(102);
            g2.setFont(fuenteCursiva);
            String titulo = "Bienvenido";
            FontMetrics fm = g2.getFontMetrics();
            int xTitulo = (w - fm.stringWidth(titulo)) / 2;
            int yTitulo = 158;

            g2.setColor(new Color(18, 55, 165));
            g2.drawString(titulo, xTitulo - 8, yTitulo - 6);
            g2.drawString(titulo, xTitulo + 6, yTitulo + 6);

            g2.setColor(Color.WHITE);
            for (int dx = -3; dx <= 3; dx++) {
                for (int dy = -3; dy <= 3; dy++) {
                    g2.drawString(titulo, xTitulo + dx, yTitulo + dy);
                }
            }

            g2.setColor(new Color(255, 38, 22));
            g2.drawString(titulo, xTitulo, yTitulo);

            // Subtítulo arcade
            g2.setFont(new Font("Impact", Font.ITALIC, 34));
            String sub = "SISTEMA DE GESTION DE CONCESIONARIA";
            int xSub = (w - g2.getFontMetrics().stringWidth(sub)) / 2;
            int ySub = 210;
            dibujarTextoConSombra(g2, sub, xSub, ySub, new Color(130, 255, 160), Color.BLACK);

            int anchoSub = g2.getFontMetrics().stringWidth(sub) + 20;
            g2.setColor(Color.BLACK);
            g2.fillRect(xSub - 8, ySub + 8, anchoSub, 10);
            g2.setColor(new Color(180, 255, 200));
            g2.fillRect(xSub - 10, ySub + 6, anchoSub, 4);
            g2.fillRect(xSub - 6, ySub + 12, anchoSub, 3);

            // Cadillac clásico
            dibujarCadillac(g2, w / 2 - 150, 255);

            // Pregunta inferior
            g2.setFont(new Font("Consolas", Font.BOLD, 22));
            String pregunta = "¿DESEA INGRESAR AL SISTEMA?";
            int xPreg = (w - g2.getFontMetrics().stringWidth(pregunta)) / 2;
            dibujarTextoConSombra(g2, pregunta, xPreg, 428, new Color(255, 230, 0), Color.BLACK);

            if (parpadeo) {
                g2.setFont(new Font("Consolas", Font.BOLD, 14));
                String hint = "INSERT COIN / SELECCIONE UNA OPCION";
                int xHint = (w - g2.getFontMetrics().stringWidth(hint)) / 2;
                dibujarTextoConSombra(g2, hint, xHint, 530, new Color(180, 255, 200), Color.BLACK);
            }
        }
    }

    // ==================== PANTALLA 2: ÁREA DE TRABAJO EN LA MISMA VENTANA ====================
    private class PanelSistema extends JPanel {
        public PanelSistema() {
            setLayout(null);

            areaChatBot = new JTextArea();
            areaChatBot.setEditable(false);
            areaChatBot.setLineWrap(true);
            areaChatBot.setWrapStyleWord(true);
            areaChatBot.setFont(new Font("Consolas", Font.BOLD, 17));
            areaChatBot.setBackground(new Color(14, 24, 18));
            areaChatBot.setForeground(new Color(130, 255, 160));
            areaChatBot.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
            areaChatBot.setText("[SISTEMA ARCADE OPERATIVO - COIN +100]\n" +
                    "Esperando instrucciones de contenido y flujo para esta pantalla...\n");

            JScrollPane scroll = new JScrollPane(areaChatBot);
            scroll.setBounds(40, 75, 815, 385);
            scroll.setBorder(BorderFactory.createLineBorder(new Color(235, 190, 65), 3));
            add(scroll);

            campoEntrada = new JTextField();
            campoEntrada.setFont(new Font("Consolas", Font.BOLD, 18));
            campoEntrada.setBackground(new Color(20, 32, 26));
            campoEntrada.setForeground(new Color(255, 230, 0));
            campoEntrada.setCaretColor(Color.WHITE);
            campoEntrada.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(235, 190, 65), 3),
                    BorderFactory.createEmptyBorder(4, 10, 4, 10)
            ));
            campoEntrada.setBounds(40, 480, 540, 44);
            campoEntrada.addActionListener(e -> procesarEntradaUsuario());
            add(campoEntrada);

            JButton btnEnviar = crearBotonArcade("ENVIAR", new Color(45, 210, 85), Color.BLACK);
            btnEnviar.setBounds(595, 480, 125, 44);
            btnEnviar.addActionListener(e -> procesarEntradaUsuario());
            add(btnEnviar);

            JButton btnSalir = crearBotonArcade("SALIR", new Color(235, 50, 40), Color.WHITE);
            btnSalir.setBounds(730, 480, 125, 44);
            btnSalir.addActionListener(e -> salirDelSistema());
            add(btnSalir);
        }

        private void procesarEntradaUsuario() {
            String texto = campoEntrada.getText().trim();
            if (texto.isEmpty()) return;
            areaChatBot.append("\nPLAYER 1 > " + texto + "\n");
            campoEntrada.setText("");
            if (texto.equalsIgnoreCase("salir") || texto.equalsIgnoreCase("exit")) {
                salirDelSistema();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            dibujarFondoConMarco(g2, getWidth(), getHeight());

            g2.setFont(new Font("Consolas", Font.BOLD, 20));
            dibujarTextoConSombra(g2, "STAGE 1: CONCESIONARIA", 40, 46, new Color(255, 230, 0), Color.BLACK);
            dibujarTextoConSombra(g2, "MODO ADMINISTRADOR", 635, 46, new Color(130, 255, 160), Color.BLACK);
        }
    }

    // ==================== MÉTODOS DE DIBUJO COMPARTIDOS ====================
    private void dibujarFondoConMarco(Graphics2D g2, int w, int h) {
        g2.setPaint(texturaFondoArcade);
        g2.fillRect(0, 0, w, h);

        GradientPaint oscurecerSup = new GradientPaint(0, 0, new Color(0, 0, 0, 115), 0, 75, new Color(0, 0, 0, 0));
        g2.setPaint(oscurecerSup);
        g2.fillRect(0, 0, w, 75);

        GradientPaint oscurecerInf = new GradientPaint(0, h - 90, new Color(0, 0, 0, 0), 0, h, new Color(0, 0, 0, 130));
        g2.setPaint(oscurecerInf);
        g2.fillRect(0, h - 90, w, 90);

        g2.setColor(new Color(45, 25, 5));
        g2.setStroke(new BasicStroke(8));
        g2.drawRect(12, 12, w - 24, h - 24);

        g2.setColor(new Color(235, 190, 65));
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(10, 10, w - 20, h - 20);
    }

    private void dibujarTextoConSombra(Graphics2D g2, String texto, int x, int y, Color colorTexto, Color colorSombra) {
        g2.setColor(colorSombra);
        g2.drawString(texto, x + 2, y + 2);
        g2.setColor(colorTexto);
        g2.drawString(texto, x, y);
    }

    private Font obtenerFuenteCursiva(int tamano) {
        String[] candidatas = {"Brush Script MT", "Segoe Script", "Lucida Handwriting", "Comic Sans MS"};
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (String cand : candidatas) {
            for (String disp : ge.getAvailableFontFamilyNames()) {
                if (disp.equalsIgnoreCase(cand)) {
                    return new Font(cand, Font.BOLD | Font.ITALIC, tamano);
                }
            }
        }
        return new Font("Serif", Font.BOLD | Font.ITALIC, tamano);
    }

    private void dibujarCadillac(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillOval(x + 10, y + 88, 285, 18);

        GeneralPath cuerpo = new GeneralPath();
        cuerpo.moveTo(x + 15, y + 75);
        cuerpo.lineTo(x + 10, y + 48);
        cuerpo.lineTo(x + 95, y + 45);
        cuerpo.lineTo(x + 125, y + 20);
        cuerpo.lineTo(x + 135, y + 20);
        cuerpo.lineTo(x + 115, y + 45);
        cuerpo.lineTo(x + 285, y + 45);
        cuerpo.lineTo(x + 295, y + 60);
        cuerpo.lineTo(x + 290, y + 78);
        cuerpo.closePath();

        g2.setColor(new Color(65, 195, 250));
        g2.fill(cuerpo);
        g2.setColor(new Color(15, 45, 95));
        g2.setStroke(new BasicStroke(3));
        g2.draw(cuerpo);

        g2.setColor(new Color(235, 245, 255));
        g2.fillRoundRect(x + 5, y + 70, 292, 10, 6, 6);
        g2.fillRect(x + 25, y + 56, 250, 4);

        g2.setColor(new Color(255, 235, 60));
        g2.fillOval(x + 286, y + 50, 10, 14);

        dibujarRueda(g2, x + 48, y + 62);
        dibujarRueda(g2, x + 215, y + 62);
    }

    private void dibujarRueda(Graphics2D g2, int rx, int ry) {
        g2.setColor(new Color(25, 25, 30));
        g2.fillOval(rx, ry, 42, 42);
        g2.setColor(Color.WHITE);
        g2.fillOval(rx + 7, ry + 7, 28, 28);
        g2.setColor(new Color(180, 195, 210));
        g2.fillOval(rx + 12, ry + 12, 18, 18);
        g2.setColor(new Color(65, 195, 250));
        g2.fillOval(rx + 17, ry + 17, 8, 8);
    }
}