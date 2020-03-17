package org.laba3;

import org.primefaces.PrimeFaces;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@ManagedBean(name = "controller")
@ApplicationScoped
public class Controller {

    private String curLogin = "public";
    private double curR = 1.5;
    private double curX = 0;
    private final int x_offset = -3;
    private int userid;
    private DatabaseInteractor dbconn;
    private double curY;
    private ArrayList<Point> data;
    //Constructor

    public Controller(){
        curLogin = "public";
        dbconn = new DatabaseInteractor();
        data = new ArrayList<>();
        onLogin(curLogin);
    }


    //Business logic

    public String onLogin(String login){
        try {
            userid = dbconn.loadOrCreateUser(login);
            data = new ArrayList<>(dbconn.loadUserData(login));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "work";
    }

    public String onRecievedPoints(){
        try {
            addPoint(curX, curY, curR);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "work";
    }


    public void onClickPoint(){
        try {
            Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

            String sx = requestParameterMap.get("pX").replace(',','.');
            String sy = requestParameterMap.get("pY").replace(',','.');
            String sr = requestParameterMap.get("pR").replace(',','.');
            //String sr2 = requestParameterMap.get("myForm:param-r").replace(',','.');
            double r= Double.parseDouble(sr);
            double x= Double.parseDouble(sx);
            double y= Double.parseDouble(sy);
            addPoint(x,y,r);


            //return addResult(x,y,r);



        } catch (Exception e) {
            e.printStackTrace();
            //return "check";
        }
    }
    private void addPoint(double x, double y, double r) throws SQLException {
        Point tmp = dbconn.insertPoint(userid, x, y, r);
        PrimeFaces.current().executeScript(tmp.redrawFunction());
        data.add(tmp);
    }
    public String onRemovePoint(Point e){
        try {
            dbconn.deletePoint(e);
            data.remove(e);
        }
        catch (Exception d){
            d.printStackTrace();
        }
        return "work";
    }
    public ArrayList<Point> getAllResults(){
        ArrayList<Point> tmp = new ArrayList<>(data);
        Collections.reverse(tmp);
        return tmp;
    }
    public void callRedraw(){
        try {
            Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            //data.clear();
            String sr = requestParameterMap.get("pR").replace(',', '.');
            double r = Double.parseDouble(sr);

            for (Point e : data) {
                PrimeFaces.current().executeScript(e.redrawFunction(r));
                e.setR(r);
                dbconn.updatePoint(e);
            }
        }
        catch(Exception e){
                e.printStackTrace();
        }
//        PrimeFaces.current().executeScript();
        return;
    }
    //Getters and Setters


    public String getCurLogin() {
        return curLogin;
    }

    public void setCurLogin(String curlogin) {
        this.curLogin = curlogin;
    }

    public double getCurY() {
        return curY;
    }

    public void setCurY(double curY) {
        this.curY = curY;
    }

    public double getCurR() {
        return curR;

    }

    public void setCurR(double curR) {
        this.curR = curR;
    }


    public double getX() {
        return curX;
    }

    public void setX(double x) {
        curX = x;
    }
}
