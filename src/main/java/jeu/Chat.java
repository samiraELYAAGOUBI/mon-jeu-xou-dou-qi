package jeu;

public class Chat extends Animal {
    public Chat(int joueur, int x, int y) {
        super("Chat", 2, joueur, x, y);
    }

    @Override
    public String getSymbole() {
        return joueur == 1 ? "chat" : "CHAT";
    }
}