def f(x, a, b, c)
  if c.to_i < 0 and b.to_i != 0
    a * x * x + b * b * x
  elsif c.to_i > 0 and b.to_i == 0
    (x + a) / (x + c)
  else
    x / c
  end
end

def F(xs, xe, dx, a, b, c)
  while xs <= xe
    print xs, " ", f(xs, a, b, c), "\n"
    xs += dx
  end
end

print "x start: "
xs = gets.chomp.to_f
print "x end: "
xe = gets.chomp.to_f
print "dx: "
dx = gets.chomp.to_f
print "a: "
a = gets.chomp.to_f
print "b: "
b = gets.chomp.to_f
print "c: "
c = gets.chomp.to_f
F(xs, xe, dx, a, b, c)