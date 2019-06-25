(

ClioLibrary.catalog ({

	~yoMama1 = {"YO MAMA".postln;};

	~yoMama2 = {"YO MAMA!!".postln;};

}, { |envir|
	"I am main.".postln;
	// p.patterns.putFromEnvir(envir);

}, { |envir|
	"I am NOT main.".postln;
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)




// (
// e = Environment; e.use {
//
// 	~yoMama1 = {"YO MAMA".postln;};
//
// 	~yoMama2 = {"YO MAMA!!".postln;};
//
// };
// if (currentEnvironment === topEnvironment,
// 	{"I AM MAIN".postln;},
// 	{"no main man".postln;}
// );
// e;
// )

