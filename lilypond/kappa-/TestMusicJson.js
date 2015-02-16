pitch -> any pitch (can use for exact pitch or scale de)

interval = [7, 5, 4, 5, 4, 1]
rhythm = [8, 8, 8, {"r":8}, {"s":8}, {"t": [8, 8, 8], "in":2}, 4]
myDynamics = new jupe.Markup();

var myInters = new jupe.Intervals([7, 5, 4, 5, 4, 0]);

pitchSequence -> (start pitch.... + sequence of steps up/down)
 - (convert) startOn
 - (convert) insert
 - (convert) append
 - (convert) augment
 - (convert) reTune
 - (convert) replace (including remove if replacing with nothing)
 - (convert) reverse
 - (convert) endOn

 rhythm -> sequence of note + rest durations
  - (convert) reverse
  - (convert) times

// BUBBLES:
/*
 - represent clusters (or bubbles) of musical material (a section, a cycle/loop, a stack, etc.)
 - can output a lilypond file with music variables
 - can contain other bubbles

*/
var myBubleData = {
	"morningP": {"pitches": [[1,2,3,4,5]]},
	"morningR": {"rhythms": [[8, 8, 8, {"r":8}, {"s":8}, {"t": [8, 8, 8], "in":2}, 4]]},
	"morningA"


	"lightRhythm":,


	"name":"bagan",
	"keyPitches":{
		"intro":80,
		"main":87
		},
	"intervalSequences":{
		"morning":[4,2,0,0,0],
		"evening":[6,4,2,0,0,0],
		"singleday":{"concat":["morning","evening"]},
		"daily":{"loop":["singleday"]}
	},
	"rhythms": {
		
		"dark":[2,2,2,2]
	},
	"wayfaring":["bottom", "top"],
	"stringsAttached":{
		"bottom":{
			"test" : []
		}
	}
}

b.addPitches("pitch1", [7,5,4,5,4,0]);
b.addMatrix("m1", [b["pitch1"], b["pitch1"], b["pitch1"], b["pitch1"], b["pitch1"]]);







myBubble = new Bubble;
myBubble.keyPitches = [];
myBubble.intervalSets = {};
myBubble.wayfaring = ["A", "B", "C"]

myBubble.wayfaring

myBubble.addMarkers(["beginning", "middle", "end"]);
