package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainFrame extends JFrame {
    private Tabulka tabulka;
    private TabulkaModel tabulkaModel;
    private JPanel colorMix;
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);

        // Vytvoření JSpinnerů
        JSpinner red = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        JSpinner green = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        JSpinner blue = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));

        // Listener pro změnu barvy na základě spinnerů
        ChangeListener changeListener = e -> updateColorDisplay(red, green, blue);
        red.addChangeListener(changeListener);
        green.addChangeListener(changeListener);
        blue.addChangeListener(changeListener);

        // Vytvoření JTextField
        JTextField colorNameField = new JTextField("Název barvy", 15);

        // Vytvoření tlačítka pro přidání barvy
        JButton addButton = new JButton("Přidat barvu");

        // Nastavení akce pro tlačítko
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nazev = colorNameField.getText();
                int r = (int) red.getValue();
                int g = (int) green.getValue();
                int b = (int) blue.getValue();
                String hex = String.format("#%02X%02X%02X", r, g, b);
                Barva barva = new Barva(nazev, r, g, b, hex);
                tabulkaModel.pridatBarvu(barva);
            }
        });

        // Vytvoření panelu pro spinnery a textové pole s tlačítkem
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        JPanel spinnerPanel = new JPanel();
        spinnerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        spinnerPanel.add(new JLabel("R:"));
        spinnerPanel.add(red);
        spinnerPanel.add(new JLabel("G:"));
        spinnerPanel.add(green);
        spinnerPanel.add(new JLabel("B:"));
        spinnerPanel.add(blue);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5)); // FlowLayout, zarovnání na střed
        bottomPanel.add(colorNameField);
        bottomPanel.add(addButton);

        // Vytvoření panelu pro zobrazení namíchané barvy
        colorMix = new JPanel();
        colorMix.setPreferredSize(new Dimension(100, 50));
        updateColorDisplay(red, green, blue); // Nastavení počáteční barvy

        controlPanel.add(spinnerPanel, BorderLayout.NORTH);
        controlPanel.add(colorMix, BorderLayout.CENTER);
        controlPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Vytvoření tabulky
        tabulkaModel = new TabulkaModel();
        tabulka = new Tabulka(tabulkaModel);
        JScrollPane scrollPane = new JScrollPane(tabulka);

        // Přidání posluchače pro kliknutí na řádek tabulky
        tabulka.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tabulka.getSelectedRow();
                    if (selectedRow != -1) {
                        Barva selectedBarva = tabulkaModel.getBarvaAt(selectedRow);
                        colorMix.setBackground(new Color(selectedBarva.getR(), selectedBarva.getG(), selectedBarva.getB()));
                        colorMix.repaint();
                    }
                }
            }
        });

        // Vytvoření tlačítka pro export
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Vyberte soubor pro uložení JSONu");
                fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".json");
                    exportToJSON(file, tabulkaModel);
                }
            }
        });

        // Vytvoření panelu pro export tlačítko
        JPanel exportPanel = new JPanel();
        exportPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        exportPanel.add(exportButton);

        // Přidání komponent do hlavního okna
        add(controlPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(exportPanel, BorderLayout.SOUTH);

        // Nastavení viditelnosti okna
        setVisible(true);
    }

    private void updateColorDisplay(JSpinner red, JSpinner green, JSpinner blue) {
        int r = (int) red.getValue();
        int g = (int) green.getValue();
        int b = (int) blue.getValue();
        colorMix.setBackground(new Color(r, g, b));
        colorMix.repaint();
    }

    private void exportToJSON(File file, TabulkaModel tabulkaModel) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(tabulkaModel.getBarvy());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
