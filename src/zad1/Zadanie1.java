package zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Zadanie1 {
    public static void main(String[] args) {
        new MyFrame();
    }
}

class MyFrame extends JFrame implements ActionListener {
    List<Wycinek> wycinki;
    List<Integer> wartosciWycinkow;
    JTextField inputField;
    JButton dodaj;
    JButton usun;
    JButton edytuj;
    JList<Integer> dane;
    ChartPanel pieChart;
    MyFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,700);
        //tworzenie elementów gui
        inputField = new JTextField();
        dodaj = new JButton("Dodaj");
        usun = new JButton("usun");
        edytuj = new JButton("edytuj");
        dane = new JList<>();
        //wartosciWycinkow = new ArrayList<>();
        //ustawienie elementow gui
        inputField.setBounds(130,100,100,20);
        dodaj.setBounds(80,170,70,30);
        usun.setBounds(150,170,70,30);
        edytuj.setBounds(220,170,70,30);
        dane.setBounds(150,370,100,200);
        //tworzenie modelu danych dla Jlist
        DefaultListModel<Integer> model1 = new DefaultListModel<>();
        dane.setModel(model1);
        wycinki = new ArrayList<>();
        //dodanie elementow gui
        add(inputField);
        add(dodaj);
        add(usun);
        add(edytuj);
        add(dane);
        //dodanie action listenera
        dodaj.addActionListener(this);
        usun.addActionListener(this);
        edytuj.addActionListener(this);
        //koncowe polecenia do ustawienia elementow i samego okienka
        setLayout(null);
        setVisible(true);
        setLocationRelativeTo(null);
    }
    public void przemaluj(DefaultListModel<Integer> model){
       /* wartosciWycinkow = Methods.wartosciPie(model);
        for(int i=0;i<wartosciWycinkow.size();i++){
            Random r = new Random();
            Color kolor = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256));
            System.out.println(kolor);
            wycinki.add(new Wycinek(wartosciWycinkow.get(i),kolor));
        }*/
        pieChart = new ChartPanel(wartosciWycinkow,wycinki);
        add(pieChart);
        pieChart.repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultListModel<Integer> model;
        if(dane.getFirstVisibleIndex()==-1){
            model = new DefaultListModel<>();
        } else model = (DefaultListModel<Integer>) dane.getModel();
        try {
            Integer input = Integer.parseInt(inputField.getText());
            if (e.getSource().equals(dodaj)) {
                System.out.println("DODAWANIE");
                model.addElement(input);
                wartosciWycinkow = Methods.wartosciPie(model);
                Random r = new Random();
                Color kolor = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256));
                Methods.aktualizujListe(wycinki,model);
                wycinki.add(new Wycinek(wartosciWycinkow.get(wartosciWycinkow.size()-1),kolor));
            } else if(e.getSource().equals(usun)&&dane.getSelectedIndex()!=-1){
                System.out.println("USUWANIE");
                System.out.println(dane.getSelectedIndex());
                System.out.println(wycinki.get(dane.getSelectedIndex()));
                wycinki.remove(dane.getSelectedIndex());
                model.remove(dane.getSelectedIndex());
                Methods.aktualizujListe(wycinki,model);
            } else if(e.getSource().equals(edytuj)&&dane.getSelectedIndex()!=-1){
                System.out.println("EDYTOWANIE");
                model.setElementAt(input,dane.getSelectedIndex());
                Methods.aktualizujListe(wycinki,model);
            }
            przemaluj(model);
            dane.setModel(model);
        } catch (NumberFormatException ex) {
            System.out.println("Wrong input.");
        }

    }
}

class ChartPanel extends JPanel{
    List<Integer> wartosciWycinkow;
    int startAngle = 0;
    List<Wycinek> wycinki;
    ChartPanel(List<Integer> modelWartosciWycinkow, List<Wycinek> modelWycinki){
        this.wycinki = modelWycinki;
        this.wartosciWycinkow = modelWartosciWycinkow;
        this.setBounds(470,170,400,400);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        setVisible(true);
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        if(wycinki.size()!=0){
            for(int i=0;i<wycinki.size();i++){
                g2D.setPaint(wycinki.get(i).kolor);
                System.out.println(wycinki.get(i).kolor);
                g2D.fillArc(10,10,380,380,startAngle,wycinki.get(i).dlugoscWycinka);
                System.out.println(wycinki.get(i).dlugoscWycinka);
                System.out.println(startAngle);
                startAngle+=wycinki.get(i).dlugoscWycinka;
            }
        }
    }
}

class Methods{
    public static List<Integer> wartosciPie(DefaultListModel<Integer> modelOG){
        List<Integer> lista = new ArrayList<>();
        double procent;
        double dlugoscWycinka;
        int calosc = 0;
        int wynik;
        for(int i=0;i<modelOG.size();i++){
            calosc += modelOG.get(i);
        }
        try{
            for(int i=0;i<modelOG.size();i++){
                procent = (double) modelOG.get(i)/calosc;
                dlugoscWycinka = 360*procent;
                wynik = (int) dlugoscWycinka;
                lista.add(wynik);
            }
        } catch (ArithmeticException e){
            System.out.println("NO DIVIDO THRU ZERO MEINE FREUNDE!");
        }
        return lista;
    }
    public static void aktualizujListe(List<Wycinek> wycinki, DefaultListModel<Integer> model){
        List<Integer> wartosciWycinkow = Methods.wartosciPie(model);
        for(int i=0;i<wycinki.size();i++) wycinki.set(i,new Wycinek(wartosciWycinkow.get(i),wycinki.get(i).kolor));
    }
}

class Wycinek{

    public int dlugoscWycinka;
    public Color kolor;

    Wycinek(int length, Color color){
        this.dlugoscWycinka = length;
        this.kolor = color;
    }
}