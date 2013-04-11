

# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice


def test1(device, test):
	ltest = test
	startActivity(device, test)
	debug(device)
	ltest+=1
	moveRamp(device, 200, 500, 200, 200, 1, ltest)
	MonkeyRunner.sleep(2.0)
	ltest+=1
	moveRamp(device, 200, 200, 500, 0, 1, ltest)
	MonkeyRunner.sleep(2.0)
	ltest+=1
	moveRamp(device, 200, 500, 200, 0, 1, ltest)
	MonkeyRunner.sleep(2.0)
	launchPenguin(device, 38, 526, test)
	MonkeyRunner.sleep(10.0)
	print "\n\n"


def test2(device, test):
	ltest = test
	startActivity(device, test)
	debug(device)
	ltest+=1
	moveRamp(device, 200, 100, 200, 200, 1, ltest)
	MonkeyRunner.sleep(2.0)
	ltest+=1
	moveRamp(device, 200, 200, 200, 0, 1, ltest)
	MonkeyRunner.sleep(2.0)
	ltest+=1
	moveRamp(device, 200, 0, 200, 300, 1, ltest)
	MonkeyRunner.sleep(2.0)
	launchPenguin(device, 38, 526, test)
	MonkeyRunner.sleep(10.0)
	print "\n\n"


def test3(device, test):
	ltest = test
	startActivity(device, test)
	debug(device)
	ltest+=1
	moveRamp(device, 200, 0, 200, 200, 1, ltest)
	MonkeyRunner.sleep(2.0)
	ltest+=1
	moveRamp(device, 200, 200, 200, 0, 1, ltest)
	MonkeyRunner.sleep(2.0)
	ltest+=1
	moveRamp(device, 200, 0, 200, 300, 1, ltest)
	MonkeyRunner.sleep(2.0)
	launchPenguin(device, 38, 526, test)
	MonkeyRunner.sleep(10.0)
	print "\n\n"


def debug(device):
# These show the package name of the currently running package and 
# the current activity's action. 
	print" package:%s" % device.getProperty('am.current.package')
	print" action:%s" % device.getProperty('am.current.action')
	print" comp.class:%s" % device.getProperty('am.current.comp.class')
	print" comp.package:%s" % device.getProperty('am.current.comp.package')
	print device.getProperty('display.width'), device.getProperty('display.height')

def lockDevice(device):
	device.press("POWER", MonkeyDevice.DOWN_AND_UP)
	return


def launchPenguin(device, x, y, test):
	device.touch(x, y, 'DOWN_AND_UP')
	print "passed launchPenguin test: %d" % (test)
	return


def moveRamp(device, xs, ys, xf, yf, len, test):
	device.drag((xs, ys), (xf, yf), len, 50)
	print "passed moveRamp test: %d" % (test)
	return


def startActivity(device, test):
	device.installPackage('Penguins-android/bin/Penguins-android.apk')
	# sets a variable with the package's internal name
	activity = "MainActivity"
	package = "com.me.mypenguins"
	runComponent = package + "/." + activity
	device.startActivity(component=runComponent)
	print "passed startActivity test: %d" % (test)
	return


def main():

	device = MonkeyRunner.waitForConnection()

	if device:
		print "Found device"
		test = 0
		test+=1
		ltest = test
		print "test: %d" % (test)
		test1(device, ltest)
		test+=1
		print "test: %d" % (test)
		test2(device, ltest)
		test+=1
		print "test: %d" % (test)
		test3(device, ltest)
		test+=1
		lockDevice(device)
		print "passed all tests"
	else:
		print "Couldn't get connection"

if __name__ == '__main__':
	main()
