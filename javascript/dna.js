
/**
 * The DNA of the rockets. This can best be described as a set of directions that the rockets will have.
 */
function DNA(genes) {

  //if genes are present, use this. Otherwise (first gen.) randomly fill the gene array. 
  if (genes) {
    this.genes = genes;
  } else {
    this.genes = [];
    for (var i = 0; i < lifespan * 10; i++) {
      this.genes[i] = p5.Vector.random2D();
      this.genes[i].setMag(maxforce);
    }
  }

  /**takes a random number ranging from zero to the maximum genes length.
   * a new gene array is created, and this is filled up with the rockets DNA, but after
   * the randomly generated point, the genes will be provided by the partner.
   * rocket sex.
   */

  this.crossover = function (partner) {
    var newgenes = [];
    var mid = floor(random(this.genes.length));
    for (var i = 0; i < this.genes.length; i++) {
      if (i > mid) {
        newgenes[i] = this.genes[i];
      } else {
        newgenes[i] = partner.genes[i];
      }
    }
    return new DNA(newgenes);
  }


  //Go over the genes, and if the condition is met, randomize the gene.
  this.mutation = function () {
    for (var i = 0; i < this.genes.length; i++) {
      if (random(1) < 0.01) {
        this.genes[i] = p5.Vector.random2D();
        this.genes[i].setMag(maxforce);
      }
    }
  }

}