package kr.neko.sokcuri.naraechat.Wrapper;

public interface TextComponentWrapper {
    Object getBaseComponent();

    int getCursorPosition();
    void setCursorPosition(int pos);
    void deleteFromCursor(int num);

    String getText();
    boolean setText(String str);

    void writeText(String str);

    void modifyText(char str);
}
