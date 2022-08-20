// Exercício: Crie uma função que receba um array. Percorra este array, some quantos números pares este array possuí e retorne a soma.

  function SomaArray () {
      const euSouUmArray = Array.from(Array(10).keys()) 
      console.log(euSouUmArray)
      let souArrayPositivo = euSouUmArray.map(y => {
              if (y % 2 == 0) {
                  return y
              }
          }) 

    let temp = [];
    for (let i of souArrayPositivo)
    i && temp.push(i);
    souArrayPositivo = temp;

          console.log(souArrayPositivo);

    const initialValue = 0;
    const somaDasArrays = souArrayPositivo.reduce(
        (previousValue, currentValue) => previousValue + currentValue,
        initialValue
    );
    console.log(somaDasArrays);
  }

  SomaArray ();
