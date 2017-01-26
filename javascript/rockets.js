function Rocket(dna) {
    this.pos = createVector(width / 2, height);
    this.vel = createVector();
    this.acc = createVector();
    this.completed = false;
    this.crashed = false;

    if (dna) {
        this.dna = dna;
    } else {
        this.dna = new DNA();
    }

    this.fitness = 0;

    //newton and stuff
    this.applyForce = function (force) {
        this.acc.add(force);
    }

    //determine how well the rocket performed
    this.calcFitness = function () {
        var d = dist(this.pos.x, this.pos.y, target.x, target.y);
        this.fitness = map(d, 0, width, width, 0);

        if (this.completed) {
            this.fitness *= 20;
        }

        if (this.crashed) {
            this.fitness /= 20;
        }
    }


    //if the rocket is still alive, update the speed, direction, etc.
    this.update = function () {
        if (!this.crashed || !this.completed) {
            var d = dist(this.pos.x, this.pos.y, target.x, target.y);

            if (d < 10) {
                this.completed = true;
                this.pos = target.copy();
            }

            //check if it has crashed. against the walls
            if (this.pos.x > width || this.pos.x < 0 || this.pos.y > height || this.pos.y < 0) {
                this.crashed = true;
            }

            //check if it has hit the obstacle (ugly method, will be replaced asap)
            if (this.pos.x > rx && this.pos.x < rx + rw && this.pos.y > ry && this.pos.y < ry + rh) {
                this.crashed = true;
            }

            if (this.pos.x > rx2 && this.pos.x < rx2 + rw2 && this.pos.y > ry2 && this.pos.y < ry2 + rh2) {
                this.crashed = true;
            }

            if (this.pos.x > rx3 && this.pos.x < rx3 + rw3 && this.pos.y > ry3 && this.pos.y < ry3 + rh3) {
                this.crashed = true;
            }

            this.applyForce(this.dna.genes[count]);

            //update the properties of the rocket
            if (!this.completed && !this.crashed) {
                this.vel.add(this.acc);
                this.pos.add(this.vel);
                this.acc.mult(0);
                this.vel.limit(4);
            }
        }
    }


    //draw the rocket, again using the p5 library magic.
    this.show = function () {
        push();
        noStroke();
        fill(255, 150);
        translate(this.pos.x, this.pos.y);
        rotate(this.vel.heading());
        rectMode(CENTER);
        rect(0, 0, 10, 2);
        pop();

    }
}
