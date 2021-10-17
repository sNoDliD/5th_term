# 1.1
print "1.1\n"
A = false; B = true; C = true
X = -24; Y = 4; Z = 8

print "a) ", (not (A or B) and (A and not B)), "\n"
print "b) ", ((Z != Y).object_id <= (6 >= Y).object_id and A or B and (X >= 1.5)), "\n"
print "c) ", ((8 - X * 2 <= Z) and (X ** 2 != Y ** 2) or (Z >= 15)), "\n"
print "d) ", ((X > 0) and (Y < 0) or Z >= (X * Y + (-Y / X)) - (-Z) * 2), "\n"
print "e) ", (not (A or B and not (C or (not A and B)))), "\n"
print "f) ", (X ** 2 + Y ** 2 >= 1 and X >= 0 and Y >= 0), "\n"
print "g) ", ((A and (C and B != B or A) or C) and B), "\n"
print "\n"

# 1.2
print "1.2\n"
x = 3
P = true
print (Math.log(x) / Math.log(1.fdiv(3)) > Math.log(0.7) / Math.log(1.fdiv(3)) \
  and (Math.sqrt(x) > x * x) and not P), "\n"
print "\n"

# 2.1
print "2.1\n"
x = 1
if -4 < x and x <= 0
  y = (x -2).abs.fdiv(x ** 2 * Math.cos(x)) ** (1.fdiv(7))
elsif 0 < x and x <= 12
  y = 1.fdiv(Math.tan(x + 1.fdiv(Math::E ** x).fdiv(Math.sin(x) ** 2)) ** (7.fdiv(2)))
elsif x < -4 or x > 12
  y = 1.fdiv(1 + x.fdiv(1 + x.fdiv(1 + x)))
end
print y, "\n\n"

# 2.2
print "2.2\n"
x = 1
case x
when -4.0..0
  y = (x -2).abs.fdiv(x ** 2 * Math.cos(x)) ** (1.fdiv(7))
when 0..12.0
  y = 1.fdiv(Math.tan(x + 1.fdiv(Math::E ** x).fdiv(Math.sin(x) ** 2)) ** (7.fdiv(2)))
else
  y = 1.fdiv(1 + x.fdiv(1 + x.fdiv(1 + x)))
end
print y, "\n\n"

# 3
print "3\n"
s = 0
(0..8).each { |i| s += 1.fdiv(3 ** i) }

print "2) ", s, "\n"
n = 10
s = 0
(0...n).each { |_| s = (2 + s) ** (1.fdiv(2)) }

print "5) ", s, "\n\n"

# 4
print "4\n"
e = 0.00001

def factorial(n)
  n > 1 ? n * factorial(n - 1) : 1
end

def f1(n)
  (factorial(n - 1).fdiv(factorial(n + 1)) ** (n * (n + 1)))
end

def f2(n)
  1.fdiv(2 * n + 1) * (-1) ** n
end

def f3(n)
  factorial(4 * n) * factorial(2 * n - 1).fdiv(factorial(4 * n + 1) * 4 ** (2 * n) * factorial(2 * n))
end

s = 0
2.step do |i|
  x = f1(i)
  s += x
  break if x.abs < e
end

print "1) ", s, "\n"

s = 0
0.step do |i|
  x = f2(i)
  s += x
  break if x.abs < e
end

print "2) ", Math::PI.fdiv(4), " && ", s, "\n"


s = 0
1.step do |i|
  x = f3(i)
  s += x
  break if x.abs < e
end

print "3) ", s, "\n"