// THIRD RUN BUSSES:
(
~me = NetAddr("127.0.0.1", 57120);
~controlBusBack = Bus.control(s,1);
OSCdef(\listenBack, func:{arg msg;
	~controlBusBack.value = msg[1];
}, path:'/pineapple/back');

~controlBusForward = Bus.control(s,1);
OSCdef(\listenForward, func:{arg msg;
	~controlBusForward.value = msg[1];
}, path:'/pineapple/forward');

~controlBusMower = Bus.control(s,1);
OSCdef(\listenMower, func:{arg msg;
	~controlBusMower.value = msg[1];
}, path:'/pineapple/mower');

~controlBusCounter = Bus.control(s,1);
OSCdef(\listenCounter, func:{arg msg;
	~controlBusCounter.value = msg[1];
}, path:'/pineapple/counter');

~controlBusBackCount = Bus.control(s,1);
OSCdef(\listenBackCount, func:{arg msg;
	~controlBusBackCount.value = msg[1];
}, path:'/pineapple/back/count');
~me.sendMsg('/pineapple/back', 0);
~me.sendMsg('/pineapple/forward', 0);
~me.sendMsg('/pineapple/mower', 0);
~me.sendMsg('/pineapple/counter', 0);
~me.sendMsg('/pineapple/back/count', 0);
)