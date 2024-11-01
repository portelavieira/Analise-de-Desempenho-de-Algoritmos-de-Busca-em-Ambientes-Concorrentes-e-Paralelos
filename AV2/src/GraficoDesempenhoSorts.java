import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GraficoDesempenhoSorts extends JPanel {
    private final Map<Integer, Map<Integer, Long>> data;
    private final int[] threadCounts;
    private final String titulo;

    public GraficoDesempenhoSorts(Map<Integer, Map<Integer, Long>> data, int[] threadCounts, String titulo) {
        this.data = data;
        this.threadCounts = threadCounts;
        this.titulo = titulo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int margin = 50;

        int numSizes = data.keySet().size();
        long maxTime = data.values().stream()
                .flatMap(m -> m.values().stream())
                .max(Long::compare)
                .orElse(1L);

        int xScale = (width - 2 * margin) / numSizes;
        int yScale = (height - 2 * margin) / (int) maxTime;

        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};

        int x = margin;
        for (int size : data.keySet()) {
            Map<Integer, Long> threadData = data.getOrDefault(size, new HashMap<>());

            for (int i = 0; i < threadCounts.length; i++) {
                int threads = threadCounts[i];
                long time = threadData.getOrDefault(threads, 0L);

                int barHeight = (int) (time * yScale);
                g2d.setColor(colors[i % colors.length]);

                g2d.fillRect(x, height - margin - barHeight, xScale / threadCounts.length, barHeight);
                x += xScale / threadCounts.length;
            }
            x += xScale - (xScale / threadCounts.length) * threadCounts.length;
        }

        g2d.setColor(Color.BLACK);
        g2d.drawLine(margin, height - margin, width - margin, height - margin);
        g2d.drawLine(margin, height - margin, margin, margin);

        int xLabel = margin;
        for (int size : data.keySet()) {
            g2d.drawString(String.valueOf(size), xLabel + (xScale / 2), height - margin + 20);
            xLabel += xScale;
        }

        for (int i = 0; i <= 10; i++) {
            int yLabel = height - margin - (i * (height - 2 * margin) / 10);
            g2d.drawString(String.valueOf(maxTime * i / 10), margin - 40, yLabel + 5);
            g2d.drawLine(margin - 5, yLabel, margin, yLabel);
        }

        g2d.drawString(titulo, width / 2 - g2d.getFontMetrics().stringWidth(titulo) / 2, margin / 2);

        // Adicionar uma legenda de cores para o número de threads
        int legendY = margin + 30;
        g2d.setColor(Color.RED);
        g2d.fillRect(margin, legendY, 15, 15);
        g2d.drawString("1 Thread (Serial)", margin + 20, legendY + 12);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(margin, legendY + 20, 15, 15);
        g2d.drawString("2 Threads", margin + 20, legendY + 32);

        g2d.setColor(Color.GREEN);
        g2d.fillRect(margin, legendY + 40, 15, 15);
        g2d.drawString("4 Threads", margin + 20, legendY + 52);

        g2d.setColor(Color.ORANGE);
        g2d.fillRect(margin, legendY + 60, 15, 15);
        g2d.drawString("8 Threads", margin + 20, legendY + 72);
    }

    public static Map<Integer, Map<Integer, Long>> loadDataFromCSV(String filePath) {
        Map<Integer, Map<Integer, Long>> data = new HashMap<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Pular o cabeçalho
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                
                // Ignorar a coluna Execucao (values[0]) e ler o Tamanho, Threads e Tempo (ms)
                int tamanhoArray = Integer.parseInt(values[1]); // Segunda coluna é o tamanho do array
                int threads = Integer.parseInt(values[2]);      // Terceira coluna é o número de threads
                long tempoExecucao = Long.parseLong(values[3]); // Quarta coluna é o tempo de execução
    
                data.putIfAbsent(tamanhoArray, new HashMap<>());
                data.get(tamanhoArray).put(threads, tempoExecucao);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter número: " + e.getMessage());
        }
        return data;
    }

    public static void main(String[] args) {
        String[] files = {
                "BubbleSort_resultados.csv",
                "MergeSort_resultados.csv",
                "QuickSort_resultados.csv",
                "InsertionSort_resultados.csv"
        };
        String[] titles = {
                "Bubble Sort Performance",
                "Merge Sort Performance",
                "Quick Sort Performance",
                "Insertion Sort Performance"
        };

        int[] threadCounts = {1, 2, 4, 8}; // Número de threads usado nos testes

        for (int i = 0; i < files.length; i++) {
            Map<Integer, Map<Integer, Long>> data = loadDataFromCSV(files[i]);

            JFrame frame = new JFrame(titles[i]);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new GraficoDesempenhoSorts(data, threadCounts, titles[i]));
            frame.setVisible(true);
        }
    }
}
