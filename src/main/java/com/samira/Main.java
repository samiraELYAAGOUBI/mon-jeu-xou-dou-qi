package com.samira;

import dao.JoueurDAO;
import dao.PartieDAO;
import interfaceconsole.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        JoueurDAO.creerTable();
        PartieDAO.creerTable();

        MenuPrincipal.lancer();
    }
}
