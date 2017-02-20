// THIRD RUN BUSSES:
(
~me = NetAddr("127.0.0.1", 57120);
~controlBusBack = Bus.control(s,1);
OSCdef(\listenBack, func:{arg msg;
	~controlBusBack.value = msg[1];
}, path:'/pineapple/back');

~controlBusBackCount = Bus.control(s,1);
OSCdef(\listenBackCount, func:{arg msg;
	~controlBusBackCount.value = msg[1];
}, path:'/pineapple/back/count');

~controlBusForward = Bus.control(s,1);
OSCdef(\listenForward, func:{arg msg;
	~controlBusForward.value = msg[1];
}, path:'/pineapple/forward');

/*~controlBusForward8 = Bus.control(s,1);
OSCdef(\listenForward8, func:{arg msg;
	~controlBusForward8.value = msg[1];
}, path:'/pineapple/forward/8');

~controlBusForward12 = Bus.control(s,1);
OSCdef(\listenForward12, func:{arg msg;
	~controlBusForward12.value = msg[1];
}, path:'/pineapple/forward/12');

~controlBusForward16 = Bus.control(s,1);
OSCdef(\listenForward16, func:{arg msg;
	~controlBusForward16.value = msg[1];
}, path:'/pineapple/forward/16');

~controlBusForward16 = Bus.control(s,1);
OSCdef(\listenForward16, func:{arg msg;
	~controlBusForward16.value = msg[1];
}, path:'/pineapple/forward/16');*/

~controlBusMower = Bus.control(s,1);
OSCdef(\listenMower, func:{arg msg;
	~controlBusMower.value = msg[1];
}, path:'/pineapple/mower');

~controlBusCounter = Bus.control(s,1);
OSCdef(\listenCounter, func:{arg msg;
	~controlBusCounter.value = msg[1];
}, path:'/pineapple/counter');

~me.sendMsg('/pineapple/forward', 0);
~me.sendMsg('/pineapple/forward/8', 0);
~me.sendMsg('/pineapple/forward/12', 0);
~me.sendMsg('/pineapple/forward/16', 0);
~me.sendMsg('/pineapple/back', 0);
~me.sendMsg('/pineapple/back/count', 1);
~me.sendMsg('/pineapple/mower', 0);
~me.sendMsg('/pineapple/counter', 1);
)