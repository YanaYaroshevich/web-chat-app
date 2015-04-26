package bsu.famcs.chat.model;

public class Message{
    private String name;
    private String text;
    private String date;
    private String id;
    private boolean isDeleted;

    public Message(String name, String text, String date, String id, boolean isDeleted){
        this.name = name;
        this.text = text;
        this.date = date;
        this.id = id;
        this.isDeleted = isDeleted;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getText(){
        return this.text;
    }

    public String getDate(){
        return this.date;
    }

    public boolean getIsDeleted(){
        return this.isDeleted;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setIsDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    public void setDate(String date){
        this.date = date;
    }

    @Override
     public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":\"").append(id).append("\", \"name\":\"").append(name)
        .append("\", \"text\":\"").append(text).append("\", \"date\":\"").append(date)
        .append("\", \"isDeleted\":\"").append(isDeleted).append("\"}");
        return sb.toString();
     }
}