<%@page contentType="text/html" pageEncoding="UTF-8" import="personcontrol.*" import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Настройка</title>
        <style type="text/css">
            .topdiv {
                color:#aaaaaa;
                margin-top:20px;
                margin-left:40px;
            }

            .title {
                font-family:calibri;
                font-size:32pt;
            }

            .baton {
                height:20px;
                background-color: #94D1FF; 
                /* border */
                border-style:none;
                border-radius:6px; 
                -webkit-border-radius:6px; 
                -moz-border-radius:5px;
                /* transition */
                transition: all 0.5s ease;
                -webkit-transition: all 0.5s ease;
                -moz-transition: all 0.5s ease;
            }
            .baton:hover {
                background-color: #FFFFFF;
                box-shadow: 0px 0px 7px rgba(255,255,255,0.8);
                -webkit-box-shadow: 0px 0px 7px rgba(255,255,255,0.8);
                -moz-box-shadow: 0px 0px 7px rgba(255,255,255,0.8);
            }

        </style>
    </head>
    <body bgcolor="#1e1e1e">
        <div class="topdiv">
            <div class="title">
                Спецконтроль
                <div style="float:right;">
                    <img src="config/persons.png" width="62" height="62">
                </div>
            </div>
            <hr style="margin-left:0px; margin-right: 100px">
            <form action="">
                <div class="title" style="font-size:14pt; position:absolute; top: 85px; left:50px;">
                    Список посетителей
                </div>
                <select name="source" size="20" multiple style="border-style:none; width:250px; position:absolute; top: 115px; left:50px;">
                    <%  ArrayList<AdpCardHolder> ls = new ArrayList<AdpCardHolder>();
                        for (int i = 0; i < 10; i++) {
                            AdpCardHolder ch = new AdpCardHolder();
                            ch.setID("ID" + i);
                            ch.setName("Name" + i);
                            ls.add(ch);
                        }
                        for (AdpCardHolder ch : ls) {
                            if (true){
                                
                            }%>
                            <option value="<%= ch.getID()%>"><%= ch.getName()%></option>
                    <%  }%>
                </select>
                <input type="button" value=" Выбрать " class="baton" style="width:70px; padding:3px; position:absolute; left:310px; top:150px;" />
                <input type="button" value=" Удалить " class="baton" style="width:70px; padding:3px; position:absolute; left:310px; top:180px;" />
                <div class="title" style="font-size:14pt; position:absolute; top: 85px; left:390px;">
                    Список спец. контроля
                </div>
                <select name="selected" size="10" style="border-style:none; width:250px; position:absolute; top: 115px; left:390px;">
                    <option><%= MainClass.fillGRCardHolders()%></option>
                </select>
                <input type="submit" value=" OK " class="baton" style="width:70px; position:absolute;top: 465px; left:300px;"></input>
                <input type="submit" value="Отменить" class="baton" style="width:70px; position:absolute;top: 465px; left:380px;"></input>
            </form>
        </div>
    </body>
</html>
