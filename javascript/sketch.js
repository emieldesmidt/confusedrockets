var population;
var lifespan = 850;
var lifeP;
var count = 0;
var target;
var maxforce = 0.4;
var gen = 1;

var rw = window.innerWidth / 1.5;
var rh = 8;
var rx = window.innerWidth / 3;
var ry = window.innerHeight / 2.2;

var rw2 = window.innerWidth / 1.5;
var rh2 = 8;
var rx2 = 0;
var ry2 = window.innerHeight / 1.8;

var rw3 = window.innerWidth / 1.5;
var rh3 = 8;
var rx3 = 0;
var ry3 = window.innerHeight / 2.8;

//setup of the program
function setup() {
  createCanvas(window.innerWidth, window.innerHeight);
  population = new Population();
  target = createVector(width / 2, 50);
}


//loop that animates
function draw() {
  background(0);
  population.run();

  var ctx = canvas.getContext("2d");
  ctx.font = "30px Arial";
  ctx.fillStyle = "#363636";
  ctx.textAlign = "center";
  ctx.fillText("Generation: " + gen, canvas.width / 2, canvas.height / 2);
  count++;

  //if the lifespan has "expended", restart the code.
  if (count == lifespan) {
    population.evaluate();
    population.selection();
    count = 0;
    gen++;
  }

  //p5 drawing functions.
  fill(255);
  rect(rx, ry, rw, rh);
  rect(rx2, ry2, rw2, rh2);
  rect(rx3, ry3, rw3, rh3);

  ellipse(target.x, target.y, 16, 16);


}
