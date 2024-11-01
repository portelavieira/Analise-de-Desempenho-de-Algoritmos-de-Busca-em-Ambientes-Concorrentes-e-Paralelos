import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraficoSimples extends JPanel {
    private List<Integer> arraySizes = new ArrayList<>();
    private List<Long> serialTimes = new ArrayList<>();
    private List<Long> parallelTimes = new ArrayList<>();
    private String chartTitle;

    public GraficoSimples(String csvFile, String chartTitle) {
        this.chartTitle = chartTitle;
        loadCSVData(csvFile);
    }

    private void loadCSVData(String csvFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line = br.readLine(); // Ignora o cabeçalho
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                arraySizes.add(Integer.parseInt(values[0]));
                serialTimes.add(Long.parseLong(values[1]));
                parallelTimes.add(Long.parseLong(values[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int padding = 50;
        int labelPadding = 25;
        int barWidth = (getWidth() - 2 * padding) / (arraySizes.size() * 2);
        int maxBarHeight = getHeight() - 2 * padding - labelPadding;
        
        // Encontra o maior tempo para ajustar a escala
        long maxTime = Math.max(
                serialTimes.stream().max(Long::compare).orElse(1L),
                parallelTimes.stream().max(Long::compare).orElse(1L)
        );

        // Desenha o título
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(chartTitle, getWidth() / 2 - g.getFontMetrics().stringWidth(chartTitle) / 2, padding / 2);

        // Desenha o eixo Y e as linhas de referência
        g.drawLine(padding, getHeight() - padding, padding, padding);
        g.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);

        for (int i = 0; i < arraySizes.size(); i++) {
            int x = padding + i * 2 * barWidth + i * 10;
            int serialBarHeight = (int) ((serialTimes.get(i) * maxBarHeight) / maxTime);
            int parallelBarHeight = (int) ((parallelTimes.get(i) * maxBarHeight) / maxTime);

            // Desenha a barra serial
            g.setColor(Color.BLUE);
            g.fillRect(x, getHeight() - padding - serialBarHeight, barWidth, serialBarHeight);
            g.setColor(Color.BLACK);
            g.drawString("S: " + serialTimes.get(i) + "ms", x, getHeight() - padding - serialBarHeight - 5);

            // Desenha a barra paralela
            g.setColor(Color.RED);
            g.fillRect(x + barWidth + 5, getHeight() - padding - parallelBarHeight, barWidth, parallelBarHeight);
            g.setColor(Color.BLACK);
            g.drawString("P: " + parallelTimes.get(i) + "ms", x + barWidth + 5, getHeight() - padding - parallelBarHeight - 5);

            // Label do tamanho do array
            g.setColor(Color.BLACK);
            String label = arraySizes.get(i) + "";
            g.drawString(label, x + barWidth / 2, getHeight() - padding + labelPadding / 2);
        }
    }

    public static void createAndShowChart(String csvFile, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new GraficoSimples(csvFile, title));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowChart("BubbleSort_resultados.csv", "Bubble Sort");
            createAndShowChart("MergeSort_resultados.csv", "Merge Sort");
            createAndShowChart("QuickSort_resultados.csv", "Quick Sort");
            createAndShowChart("InsertionSort_resultados.csv", "Insertion Sort");
        });
    }
}
