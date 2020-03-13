function ShowTime() {
    document.getElementById("myclocks").innerHTML=new Date().toLocaleString();

}
setInterval(() => {
    ShowTime();
}, 10000);
ShowTime();
