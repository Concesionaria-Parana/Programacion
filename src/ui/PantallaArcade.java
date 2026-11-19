package ui;

import model.Automovil;
import model.Camioneta;
import model.Cliente;
import model.Motocicleta;
import model.Vehiculo;
import service.CatalogoService;
import service.ConcesionariaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class PantallaArcade extends JFrame {
    private static final String CARD_BIENVENIDO = "BIENVENIDO";
    private static final String CARD_SISTEMA = "SISTEMA";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedorPrincipal = new JPanel(cardLayout);
    private final TexturePaint texturaFondoArcade;

    private final CatalogoService catalogoService = new CatalogoService();
    private final ConcesionariaService concesionariaService = new ConcesionariaService();

    private boolean parpadeo = true;
    private JTextArea areaChatBot;
    private JTextField campoEntrada;

    private enum EstadoFlujo {
        MENU_PRINCIPAL,
        FILTRO_MODELOS_MARCA,
        FILTRO_MODELOS_TIPO,
        REG_VEH_TIPO,
        REG_VEH_VIN,
        REG_VEH_MODELO,
        REG_VEH_SUCURSAL,
        REG_VEH_COLOR,
        REG_VEH_CONDICION,
        REG_VEH_TIPO_VEHICULO,
        REG_VEH_KM,
        REG_VEH_PRECIO,
        REG_VEH_EXTRA_1,
        REG_VEH_EXTRA_2,
        REG_CLI_TIPO_DOC,
        REG_CLI_NUM_DOC,
        REG_CLI_NOMBRE,
        REG_CLI_DOMICILIO,
        REG_CLI_TELEFONO,
        REG_CLI_EMAIL
    }

    private EstadoFlujo estadoActual = EstadoFlujo.MENU_PRINCIPAL;
    private Integer tempIdMarcaFiltro;
    private int tempTipoVehiculoAlta;
    private String tempVin;
    private int tempIdModelo;
    private int tempIdSucursal;
    private int tempIdColor;
    private int tempIdCondicion;
    private int tempIdTipoVehiculo;
    private int tempKilometraje;
    private BigDecimal tempPrecioVenta;
    private String tempExtra1;
    private Cliente tempCliente;

    public PantallaArcade() {
        setTitle("CONCESIONARIA '93 - CAPCOM ARCADE SYSTEM");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        estadoActual = EstadoFlujo.MENU_PRINCIPAL;
        areaChatBot.setText("[SISTEMA ARCADE OPERATIVO - COIN +100]\n");
        mostrarMenuAcciones();
        SwingUtilities.invokeLater(() -> campoEntrada.requestFocusInWindow());
    }

    private void mostrarMenuAcciones() {
        estadoActual = EstadoFlujo.MENU_PRINCIPAL;
        areaChatBot.append(
                "\n=== ACCIONES DISPONIBLES ===\n" +
                " 1 - Listar sucursales\n" +
                " 2 - Listar marcas\n" +
                " 3 - Listar tipos de vehiculo\n" +
                " 4 - Listar condiciones\n" +
                " 5 - Listar colores\n" +
                " 6 - Listar modelos (con filtros opcionales)\n" +
                " 7 - Listar vehiculos registrados\n" +
                " 8 - Registrar vehiculo\n" +
                " 9 - Listar clientes\n" +
                "10 - Registrar cliente\n" +
                "11 - Salir\n" +
                "(Escriba 'menu' en cualquier momento para cancelar y volver aqui)\n" +
                "Seleccione una opcion (1-11):\n"
        );
        desplazarAlFinal();
    }

    private void desplazarAlFinal() {
        areaChatBot.setCaretPosition(areaChatBot.getDocument().getLength());
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

            g2.setFont(new Font("Consolas", Font.BOLD, 20));
            dibujarTextoConSombra(g2, "PLAYER 1  000000", 35, 44, new Color(255, 230, 0), Color.BLACK);
            dibujarTextoConSombra(g2, "TOP SCORE  019300", 335, 44, new Color(255, 90, 210), Color.BLACK);
            dibujarTextoConSombra(g2, "UTN - PROG II", 685, 44, Color.WHITE, Color.BLACK);

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

            dibujarCadillac(g2, w / 2 - 150, 255);

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
            areaChatBot.setText("[SISTEMA ARCADE OPERATIVO - COIN +100]\n");
            mostrarMenuAcciones();

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
                return;
            }

            if (texto.equalsIgnoreCase("menu") || texto.equalsIgnoreCase("ayuda") || texto.equals("0")) {
                mostrarMenuAcciones();
                return;
            }

            try {
                switch (estadoActual) {
                    case MENU_PRINCIPAL:
                        ejecutarOpcionMenu(texto);
                        break;
                    case FILTRO_MODELOS_MARCA:
                        tempIdMarcaFiltro = parsearEnteroOpcional(texto);
                        estadoActual = EstadoFlujo.FILTRO_MODELOS_TIPO;
                        areaChatBot.append("Ingrese ID de tipo de vehiculo para filtrar (o Enter/0 para todos):\n");
                        break;
                    case FILTRO_MODELOS_TIPO:
                        Integer idTipoFiltro = parsearEnteroOpcional(texto);
                        imprimirFilas(catalogoService.listarModelos(tempIdMarcaFiltro, idTipoFiltro), "MODELOS");
                        mostrarMenuAcciones();
                        break;
                    case REG_VEH_TIPO:
                        tempTipoVehiculoAlta = Integer.parseInt(texto);
                        if (tempTipoVehiculoAlta < 1 || tempTipoVehiculoAlta > 3) {
                            areaChatBot.append("Opcion invalida. Ingrese 1 (Automovil), 2 (Camioneta) o 3 (Motocicleta):\n");
                            return;
                        }
                        estadoActual = EstadoFlujo.REG_VEH_VIN;
                        areaChatBot.append("Ingrese VIN:\n");
                        break;
                    case REG_VEH_VIN:
                        tempVin = texto;
                        estadoActual = EstadoFlujo.REG_VEH_MODELO;
                        areaChatBot.append("Ingrese ID de modelo:\n");
                        break;
                    case REG_VEH_MODELO:
                        tempIdModelo = Integer.parseInt(texto);
                        estadoActual = EstadoFlujo.REG_VEH_SUCURSAL;
                        areaChatBot.append("Ingrese ID de sucursal:\n");
                        break;
                    case REG_VEH_SUCURSAL:
                        tempIdSucursal = Integer.parseInt(texto);
                        estadoActual = EstadoFlujo.REG_VEH_COLOR;
                        areaChatBot.append("Ingrese ID de color:\n");
                        break;
                    case REG_VEH_COLOR:
                        tempIdColor = Integer.parseInt(texto);
                        estadoActual = EstadoFlujo.REG_VEH_CONDICION;
                        areaChatBot.append("Ingrese ID de condicion:\n");
                        break;
                    case REG_VEH_CONDICION:
                        tempIdCondicion = Integer.parseInt(texto);
                        estadoActual = EstadoFlujo.REG_VEH_TIPO_VEHICULO;
                        areaChatBot.append("Ingrese ID de tipo de vehiculo:\n");
                        break;
                    case REG_VEH_TIPO_VEHICULO:
                        tempIdTipoVehiculo = Integer.parseInt(texto);
                        estadoActual = EstadoFlujo.REG_VEH_KM;
                        areaChatBot.append("Ingrese kilometraje:\n");
                        break;
                    case REG_VEH_KM:
                        tempKilometraje = Integer.parseInt(texto);
                        estadoActual = EstadoFlujo.REG_VEH_PRECIO;
                        areaChatBot.append("Ingrese precio de venta (ej: 15000000.00):\n");
                        break;
                    case REG_VEH_PRECIO:
                        tempPrecioVenta = new BigDecimal(texto);
                        estadoActual = EstadoFlujo.REG_VEH_EXTRA_1;
                        if (tempTipoVehiculoAlta == 1) {
                            areaChatBot.append("Ingrese carroceria del automovil:\n");
                        } else if (tempTipoVehiculoAlta == 2) {
                            areaChatBot.append("Ingrese carroceria de la camioneta:\n");
                        } else {
                            areaChatBot.append("Ingrese tipo de motocicleta:\n");
                        }
                        break;
                    case REG_VEH_EXTRA_1:
                        tempExtra1 = texto;
                        estadoActual = EstadoFlujo.REG_VEH_EXTRA_2;
                        if (tempTipoVehiculoAlta == 1) {
                            areaChatBot.append("Ingrese cantidad de puertas:\n");
                        } else if (tempTipoVehiculoAlta == 2) {
                            areaChatBot.append("Ingrese traccion (ej: 4x4, 4x2):\n");
                        } else {
                            areaChatBot.append("Ingrese cilindrada (numero entero):\n");
                        }
                        break;
                    case REG_VEH_EXTRA_2:
                        Vehiculo nuevoVehiculo = construirVehiculoConExtra(texto);
                        int idVehGenerado = concesionariaService.registrarVehiculo(nuevoVehiculo);
                        areaChatBot.append("Vehiculo registrado con ID: " + idVehGenerado + "\n");
                        mostrarMenuAcciones();
                        break;
                    case REG_CLI_TIPO_DOC:
                        tempCliente = new Cliente();
                        tempCliente.setTipoDocumento(texto);
                        estadoActual = EstadoFlujo.REG_CLI_NUM_DOC;
                        areaChatBot.append("Ingrese numero de documento:\n");
                        break;
                    case REG_CLI_NUM_DOC:
                        tempCliente.setNumeroDocumento(texto);
                        estadoActual = EstadoFlujo.REG_CLI_NOMBRE;
                        areaChatBot.append("Ingrese nombre completo:\n");
                        break;
                    case REG_CLI_NOMBRE:
                        tempCliente.setNombreCompleto(texto);
                        estadoActual = EstadoFlujo.REG_CLI_DOMICILIO;
                        areaChatBot.append("Ingrese domicilio:\n");
                        break;
                    case REG_CLI_DOMICILIO:
                        tempCliente.setDomicilio(texto);
                        estadoActual = EstadoFlujo.REG_CLI_TELEFONO;
                        areaChatBot.append("Ingrese telefono:\n");
                        break;
                    case REG_CLI_TELEFONO:
                        tempCliente.setTelefono(texto);
                        estadoActual = EstadoFlujo.REG_CLI_EMAIL;
                        areaChatBot.append("Ingrese email:\n");
                        break;
                    case REG_CLI_EMAIL:
                        tempCliente.setEmail(texto);
                        int idCliGenerado = concesionariaService.registrarCliente(tempCliente);
                        areaChatBot.append("Cliente registrado con ID: " + idCliGenerado + "\n");
                        tempCliente = null;
                        mostrarMenuAcciones();
                        break;
                    default:
                        mostrarMenuAcciones();
                        break;
                }
            } catch (NumberFormatException ex) {
                areaChatBot.append("Entrada numerica invalida. Intente nuevamente o escriba 'menu'.\n");
            } catch (SQLException ex) {
                areaChatBot.append("Error de base de datos: " + ex.getMessage() + "\n");
                mostrarMenuAcciones();
            }
            desplazarAlFinal();
        }

        private void ejecutarOpcionMenu(String opcion) throws SQLException {
            switch (opcion) {
                case "1":
                    imprimirFilas(catalogoService.listarSucursales(), "SUCURSALES");
                    mostrarMenuAcciones();
                    break;
                case "2":
                    imprimirFilas(catalogoService.listarMarcas(), "MARCAS");
                    mostrarMenuAcciones();
                    break;
                case "3":
                    imprimirFilas(catalogoService.listarTiposVehiculo(), "TIPOS DE VEHICULO");
                    mostrarMenuAcciones();
                    break;
                case "4":
                    imprimirFilas(catalogoService.listarCondiciones(), "CONDICIONES");
                    mostrarMenuAcciones();
                    break;
                case "5":
                    imprimirFilas(catalogoService.listarColores(), "COLORES");
                    mostrarMenuAcciones();
                    break;
                case "6":
                    tempIdMarcaFiltro = null;
                    estadoActual = EstadoFlujo.FILTRO_MODELOS_MARCA;
                    areaChatBot.append("Ingrese ID de marca para filtrar (o 0 para todas):\n");
                    break;
                case "7":
                    List<Vehiculo> vehiculos = concesionariaService.listarVehiculos();
                    areaChatBot.append("--- VEHICULOS (" + vehiculos.size() + ") ---\n");
                    for (Vehiculo v : vehiculos) {
                        areaChatBot.append("ID " + v.getIdVehiculo() + " | VIN: " + v.getVin() +
                                " | Precio: " + v.getPrecioVenta() + " | KM: " + v.getKilometraje() + "\n");
                    }
                    mostrarMenuAcciones();
                    break;
                case "8":
                    estadoActual = EstadoFlujo.REG_VEH_TIPO;
                    areaChatBot.append("Tipo de vehiculo a registrar: 1=Automovil, 2=Camioneta, 3=Motocicleta\n");
                    break;
                case "9":
                    List<Cliente> clientes = concesionariaService.listarClientes();
                    areaChatBot.append("--- CLIENTES (" + clientes.size() + ") ---\n");
                    for (Cliente c : clientes) {
                        areaChatBot.append("ID " + c.getIdCliente() + " | " + c.getNombreCompleto() +
                                " | Doc: " + c.getTipoDocumento() + " " + c.getNumeroDocumento() + "\n");
                    }
                    mostrarMenuAcciones();
                    break;
                case "10":
                    estadoActual = EstadoFlujo.REG_CLI_TIPO_DOC;
                    areaChatBot.append("Ingrese tipo de documento del cliente (ej: DNI):\n");
                    break;
                case "11":
                    salirDelSistema();
                    break;
                default:
                    areaChatBot.append("Opcion no reconocida.\n");
                    mostrarMenuAcciones();
                    break;
            }
        }

        private Integer parsearEnteroOpcional(String texto) {
            if (texto.isEmpty() || texto.equals("0")) {
                return null;
            }
            return Integer.parseInt(texto);
        }

        private Vehiculo construirVehiculoConExtra(String extra2) {
            if (tempTipoVehiculoAlta == 1) {
                Automovil auto = new Automovil();
                configurarBaseVehiculo(auto);
                auto.setCarroceria(tempExtra1);
                auto.setCantidadPuertas(Integer.parseInt(extra2));
                return auto;
            } else if (tempTipoVehiculoAlta == 2) {
                Camioneta cam = new Camioneta();
                configurarBaseVehiculo(cam);
                cam.setCarroceria(tempExtra1);
                cam.setTraccion(extra2);
                return cam;
            } else {
                Motocicleta moto = new Motocicleta();
                configurarBaseVehiculo(moto);
                moto.setTipoMotocicleta(tempExtra1);
                moto.setCilindrada(Integer.parseInt(extra2));
                return moto;
            }
        }

        private void configurarBaseVehiculo(Vehiculo v) {
            v.setVin(tempVin);
            v.setIdModelo(tempIdModelo);
            v.setIdSucursal(tempIdSucursal);
            v.setIdColor(tempIdColor);
            v.setIdCondicion(tempIdCondicion);
            v.setIdTipoVehiculo(tempIdTipoVehiculo);
            v.setKilometraje(tempKilometraje);
            v.setPrecioVenta(tempPrecioVenta);
        }

        private void imprimirFilas(List<?> filas, String titulo) {
            areaChatBot.append("--- " + titulo + " (" + filas.size() + ") ---\n");
            if (filas.isEmpty()) {
                areaChatBot.append("(Sin registros)\n");
                return;
            }
            for (Object fila : filas) {
                areaChatBot.append(fila + "\n");
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