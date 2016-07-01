
Quarks.update("ArduinoSMS");
Quarks.install("ArduinoSMS");

a = ArduinoSMS("/dev/tty.usbmodem1421", 115200);
a.open;

SerialPort.listDevices;

(
p = SerialPort(
    "/dev/tty.usbmodem1421",    //edit to match your port.
    baudrate: 115200,    //check that baudrate is the same as in arduino sketch
    crtscts: true);
)

(
a.action = { |... msg| msg.postln };
)

(
r = Routine({
	var byte, str, res, val, reset = false;
	inf.do{|i|
		val = p.read;
		(val==255).if({
			postln("------");
			4.do{|i|
				val = p.read;
				postln(val);
			};
			1.wait;
		});
	};
});
)

p.close;
r.play;
r.resume;
r.stop;


//send serial data - slow pulsating
(
r= Routine({
    var byte, str, res, val, reset = 0;
    999.do{|i|
		val = p.read;
		(val == 255).if({
			postln("YO");
		}, {
			postln(val);

		}
		);
		clear;
    };


}).play;

)

r.freeAll;
r.stop;
p.close;