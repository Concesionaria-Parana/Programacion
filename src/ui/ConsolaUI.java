package ui;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ConsolaUI {
    public static final String RESET = "\u001B[0m";
    public static final String NEGRITA = "\u001B[1m";
    public static final String PARPADEO = "\u001B[5m";

    // Paleta Cadillacs and Dinosaurs (Capcom CPS-1)
    public static final String ROJO_CADILLAC = "\u001B[38;2;255;45;30m";
    public static final String SOMBRA_AZUL = "\u001B[38;2;30;80;200m";
    public static final String VERDE_DINO_1 = "\u001B[38;2;0;220;90m";
    public static final String VERDE_DINO_2 = "\u001B[38;2;90;255;140m";
    public static final String VERDE_DINO_3 = "\u001B[38;2;200;255;210m";
    public static final String CELESTE_AUTO = "\u001B[38;2;80;210;255m";
    public static final String ARENA_DESIERTO = "\u001B[38;2;235;185;110m";
    public static final String ORO_MARIO = "\u001B[38;2;255;225;0m";
    public static final String BLANCO = "\u001B[38;2;255;255;255m";
    public static final String ORO_CAPCOM = ORO_MARIO;

    public static void inicializar() {
        try {
            new ProcessBuilder("cmd", "/c", "chcp 65001 > nul").inheritIO().start().waitFor();
        } catch (Exception ignored) {
        }
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    }

    public static void limpiarPantalla() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public static void mostrarPantallaBienvenido(int registrosActuales) {
        limpiarPantalla();
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String bordeSup = ARENA_DESIERTO + "\u2554" + "\u2550".repeat(82) + "\u2557" + RESET;
        String bordeInf = ARENA_DESIERTO + "\u255A" + "\u2550".repeat(82) + "\u255D" + RESET;

        System.out.println(bordeSup);
        System.out.printf(ARENA_DESIERTO + "\u2551  " + ORO_MARIO + NEGRITA + "%-26s" + CELESTE_AUTO + "%-28s" + BLANCO + "%24s  " + ARENA_DESIERTO + "\u2551%n" + RESET,
                "1P ADMIN: " + String.format("%06d", registrosActuales * 100), "CONCESIONARIA '93", "TIME " + hora);
        System.out.println(ARENA_DESIERTO + "\u2551" + " ".repeat(82) + "\u2551" + RESET);

        // Logo estilo Cadillacs and Dinosaurs (rojo arriba, degradado verde scanline abajo)
        String[] logo = {
            "  \u2588\u2588\u2588\u2588\u2557 \u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2588\u2557   \u2588\u2588\u2557\u2588\u2588\u2557   \u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2588\u2557   \u2588\u2588\u2557\u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2588\u2588\u2588\u2588\u2557   ",
            "  \u2588\u2588\u2554\u2550\u2588\u2588\u2557\u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u2550\u2550\u255D\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2551\u2588\u2588\u2551   \u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u2550\u2550\u255D\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557\u2588\u2588\u2554\u2550\u2550\u2550\u2588\u2588\u2557  ",
            "  \u2588\u2588\u2588\u2588\u2588\u2554\u255D\u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2554\u2588\u2588\u2557 \u2588\u2588\u2551\u2588\u2588\u2551   \u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2554\u2588\u2588\u2557 \u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2551  \u2588\u2588\u2551\u2588\u2588\u2551   \u2588\u2588\u2551  ",
            "  \u2588\u2588\u2554\u2550\u2588\u2588\u2557\u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u255D  \u2588\u2588\u2551\u255A\u2588\u2588\u2557\u2588\u2588\u2551\u255A\u2588\u2588\u2557 \u2588\u2588\u2554\u255D\u2588\u2588\u2554\u2550\u2550\u255D  \u2588\u2588\u2551\u255A\u2588\u2588\u2557\u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2551  \u2588\u2588\u2551\u2588\u2588\u2551   \u2588\u2588\u2551  ",
            "  \u2588\u2588\u2588\u2588\u2588\u2554\u255D\u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2551 \u255A\u2588\u2588\u2588\u2588\u2551 \u255A\u2588\u2588\u2588\u2588\u2554\u255D \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2551 \u255A\u2588\u2588\u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255D\u255A\u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255D  ",
            "  \u255A\u2550\u2550\u2550\u2550\u255D \u255A\u2550\u255D\u255A\u2550\u2550\u2550\u2550\u2550\u2550\u255D\u255A\u2550\u255D  \u255A\u2550\u2550\u2550\u255D  \u255A\u2550\u2550\u2550\u255D  \u255A\u2550\u2550\u2550\u2550\u2550\u2550\u255D\u255A\u2550\u255D  \u255A\u2550\u2550\u2550\u255D\u255A\u2550\u255D\u255A\u2550\u2550\u2550\u2550\u2550\u255D  \u255A\u2550\u2550\u2550\u2550\u2550\u255D   "
        };

        String[] coloresLogo = {ROJO_CADILLAC, ROJO_CADILLAC, VERDE_DINO_1, VERDE_DINO_2, VERDE_DINO_3, SOMBRA_AZUL};
        for (int i = 0; i < logo.length; i++) {
            System.out.println(ARENA_DESIERTO + "\u2551   " + coloresLogo[i] + NEGRITA + logo[i] + ARENA_DESIERTO + "   \u2551" + RESET);
        }

        // Cadillac pixelado en la ruta
        System.out.println(ARENA_DESIERTO + "\u2551" + " ".repeat(82) + "\u2551" + RESET);
        System.out.println(ARENA_DESIERTO + "\u2551" + CELESTE_AUTO + NEGRITA + "                        ______//________                                          " + ARENA_DESIERTO + "\u2551" + RESET);
        System.out.println(ARENA_DESIERTO + "\u2551" + CELESTE_AUTO + NEGRITA + "                    ___/  [ADMIN]  |  []\\____                                     " + ARENA_DESIERTO + "\u2551" + RESET);
        System.out.println(ARENA_DESIERTO + "\u2551" + BLANCO + NEGRITA + "                   (____(O)___________(O)____)                                    " + ARENA_DESIERTO + "\u2551" + RESET);
        System.out.println(ARENA_DESIERTO + "\u2551" + ARENA_DESIERTO + "  \u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593\u2593  " + ARENA_DESIERTO + "\u2551" + RESET);
        System.out.println(bordeInf);

        System.out.println();
        System.out.println(VERDE_DINO_2 + NEGRITA + "  \u25B6 ¿DESEA INGRESAR AL SISTEMA?  " +
                ORO_MARIO + "[ SI ] " + BLANCO + "/ " + ROJO_CADILLAC + "[ NO ]   " +
                ARENA_DESIERTO + "(Escribí NO o presioná Ctrl+C para salir)" + RESET);
        System.out.print(CELESTE_AUTO + NEGRITA + "  PLAYER 1 > " + RESET);
    }

    public static void mensajeBot(String texto) {
        System.out.print(VERDE_DINO_1 + NEGRITA + "\n[BOT-CADILLAC] " + CELESTE_AUTO);
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            pausa(10);
        }
        System.out.println(RESET);
    }

    public static void mensajeMoneda(String texto) {
        sonidoMonedaMario();
        System.out.println(ORO_MARIO + NEGRITA + "  \u25C9 [COIN +100] " + VERDE_DINO_2 + texto + RESET);
    }

    public static void mensajeError(String texto) {
        sonidoError8Bit();
        System.out.println(ROJO_CADILLAC + NEGRITA + "  \u2716 " + texto + RESET);
    }

    public static void sonidoMonedaMario() {
        new Thread(() -> {
            tocarOndaCuadrada(987.77, 85, 0.25);
            tocarOndaCuadrada(1318.51, 320, 0.25);
        }).start();
    }

    public static void sonidoError8Bit() {
        new Thread(() -> {
            tocarOndaCuadrada(220.0, 120, 0.25);
            tocarOndaCuadrada(155.56, 220, 0.25);
        }).start();
    }

    private static void tocarOndaCuadrada(double frecuenciaHz, int duracionMs, double volumen) {
        float sampleRate = 44100f;
        int numSamples = (int) ((duracionMs / 1000.0) * sampleRate);
        byte[] buffer = new byte[numSamples];
        double periodo = sampleRate / frecuenciaHz;

        for (int i = 0; i < numSamples; i++) {
            double envolvente = 1.0 - ((double) i / numSamples) * 0.4;
            buffer[i] = (i % periodo) < (periodo / 2.0)
                    ? (byte) (127 * volumen * envolvente)
                    : (byte) (-127 * volumen * envolvente);
        }

        try {
            AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, false);
            try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
                line.open(format);
                line.start();
                line.write(buffer, 0, buffer.length);
                line.drain();
            }
        } catch (Exception ignored) {
        }
    }

    private static void pausa(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}