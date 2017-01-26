
//population constructor, manages the rocket population.
function Population() {
    this.rockets = [];
    this.popsize = 200;
    this.matingpool = [];
    this.maxfit = 0;

    //create the desired amount of rockets.
    for (var i = 0; i < this.popsize; i++) {
        this.rockets[i] = new Rocket();
    }

    //function that is called at the end of the generation lifetime.
    this.evaluate = function () {

        /**finds the rocket with the best fitness.
         * maxfit will later be used to normalize the other values.
         */
        for (var i = 0; i < this.popsize; i++) {
            this.rockets[i].calcFitness();
            if (this.rockets[i].fitness > this.maxfit) {
                this.maxfit = this.rockets[i].fitness;
            }
        }


        //iterates over all the rockets and makes their fitness relative to the maxfit
        for (var i = 0; i < this.popsize; i++) {
            this.rockets[i].fitness /= this.maxfit;
        }

        /**
         * create an array to hold the new "parent" rockets
         * rockets with the highest fitness levels will account for a significantly larger
         * share in the matingpool.
         */
        this.matingpool = [];
        for (var i = 0; i < this.popsize; i++) {
            var n = this.rockets[i].fitness * 100;
            for (var j = 0; j < n; j++) {
                this.matingpool.push(this.rockets[i]);
            }
        }
    }

    /**
     * This function takes two random rockets from the matingpool and mixes their genes.
     * This somewhat follows the principles of crossover as illustrated in highschool.
     * Also the mutation() method is called on the child. Rocket might get downs.
     */
    this.selection = function () {
        var newRockets = [];
        for (var i = 0; i < this.rockets.length; i++) {
            var parentA = random(this.matingpool).dna;
            var parentB = random(this.matingpool).dna;
            var child = parentA.crossover(parentB);
            child.mutation();
            newRockets[i] = new Rocket(child);
        }
        this.rockets = newRockets;
    }

    //manages the individual rockets
    this.run = function () {
        for (var i = 0; i < this.popsize; i++) {
            this.rockets[i].update();
            this.rockets[i].show();
        }
    }
}