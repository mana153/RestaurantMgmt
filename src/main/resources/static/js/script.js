window.addEventListener("scroll", function(){

    const navbar = document.querySelector(".navbar");

    if(window.scrollY > 50){
        navbar.style.background = "#000";
    }
    else{
        navbar.style.background = "rgba(0,0,0,0.6)";
    }

});