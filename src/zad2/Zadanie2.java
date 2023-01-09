package zad2;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Zadanie2 {
    public static void main(String[] args) {
        new MyFrame();
    }
}

class MyFrame extends JFrame implements ActionListener {
    final int N = 10;
    List<JCheckBox> checkBoxy = Methods.init_checkBoxy(N);
    List<JLabel> labelTest = Methods.init_labelTest(N);
    List<Integer> wysokosciSlupkow;
    List<Slupek> slupeks = Methods.init_Slupki(N);
    ChartPanel chart;
    List<Integer> inputs = Methods.init_inputs(N);
    List<JTextField> polaTekstowe = Methods.init_polaTekstowe(N);
    int x=130;
    int y=100;
    public MyFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,700);
        for(int i=0;i<polaTekstowe.size();i++){
            polaTekstowe.get(i).setBounds(x,y,50,15);
            checkBoxy.get(i).setBounds(x+70,y-18,50,50);
            labelTest.get(i).setBounds(300,y,150,15);
            y+=40;
            checkBoxy.get(i).addActionListener(this);
            add(polaTekstowe.get(i));
            add(checkBoxy.get(i));
            add(labelTest.get(i));
        }
        for(JTextField element : polaTekstowe){
            element.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    operate();
                }
                public void removeUpdate(DocumentEvent e) {
                    operate();
                }
                public void insertUpdate(DocumentEvent e) {
                    operate();
                }
            });
        }
        setLayout(null);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    public void przemaluj(){
        chart = new ChartPanel(slupeks);
        add(chart);
        chart.repaint();
    }
    public void operate(){
        for(int i=0;i<checkBoxy.size();i++){
            if(checkBoxy.get(i).isSelected()){
                System.out.println("selected " + i);
                try{
                    int input = Integer.parseInt(polaTekstowe.get(i).getText());
                    inputs.set(i,input);
                } catch (NumberFormatException ex){
                    System.out.println("Wrong input.");
                    inputs.set(i,0);
                }
            } else inputs.set(i,0);
        }
        wysokosciSlupkow = Methods.obliczanie_Wysokosci(inputs,N);
        for(int i=0;i<slupeks.size();i++) slupeks.set(i,new Slupek(wysokosciSlupkow.get(i),slupeks.get(i).color));
        przemaluj();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        operate();
    }
}

class ChartPanel extends JPanel{
    int x = 10;
    int y = 0;
    List<Slupek> slupeks;
    public ChartPanel(List<Slupek> slupki) {
        this.slupeks = slupki;
        this.setBounds(470,170,400,400);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        setVisible(true);
    }
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        for(int i=0;i<slupeks.size();i++){
            if(slupeks.get(i).height!=0){
                y=400-slupeks.get(i).height*3;
                g2D.setPaint(slupeks.get(i).color);
                g2D.fillRect(x,y,30,slupeks.get(i).height*3);
                x+=40;
            }
        }
    }
}

class Methods{
    public static List<JTextField> init_polaTekstowe(int n){
        List<JTextField> pola = new ArrayList<>();
        for(int i=0;i<n;i++) pola.add(new JTextField());
        return pola;
    }
    public static List<JCheckBox> init_checkBoxy(int n){
        List<JCheckBox> checkBoxes = new ArrayList<>();
        for(int i=0;i<n;i++) checkBoxes.add(new JCheckBox());
        return checkBoxes;
    }
    public static List<JLabel> init_labelTest(int n){
        List<JLabel> checkBoxes = new ArrayList<>();
        for(int i=0;i<n;i++) checkBoxes.add(new JLabel());
        return checkBoxes;
    }
    public static List<Slupek> init_Slupki(int n){
        Random r = new Random();
        List<Slupek> slupeks = new ArrayList<>();
        for(int i=0;i<n;i++) slupeks.add(new Slupek(0,new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256))));
        return slupeks;
    }
    public static List<Integer> init_inputs(int n){
        List<Integer> wysokosci = new ArrayList<>();
        for(int i=0;i<n;i++) wysokosci.add(0);
        return wysokosci;
    }
    public static List<Integer> obliczanie_Wysokosci(List<Integer> inputs, int n){
        List<Integer> wysokosci = new ArrayList<>();
        double XD;
        for(int i=0;i<n;i++) wysokosci.add(0);
        int calosc=0;
        for(int i=0;i<n;i++) calosc+=inputs.get(i);
        for(int i=0;i<n;i++) {
            try {
                XD = (double) inputs.get(i) / calosc *100;
                if(XD!=0&&XD<1) XD=1; //zabezpieczenie przed usunieciem slupka, co dzieje przy ogromnych roznicach procentowych
                wysokosci.set(i, (int) Math.round(XD));
            } catch (ArithmeticException e) {
                System.out.println("NO DIVIDO THRU ZERO MEINE FREUNDE!");
                wysokosci.set(i,0);
                System.out.println(wysokosci.get(i));
            }
        }
        return wysokosci;
    }
}

class Slupek{
    public int height;
    public Color color;
    public Slupek(int wysokosc, Color kolor){
    this.height = wysokosc;
    this.color = kolor;
    }
}
