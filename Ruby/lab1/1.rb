print "Enter x: "
x = gets.chomp.to_f
print "Enter a: "
a = gets.chomp.to_f
print "Enter b: "
b = gets.chomp.to_f

L = (6.2 * 10 ** 2.7 + Math.tan(Math::PI - x ** 3)) /
  (Math::E ** (x / a) + Math.log((b ** 2).abs)) +
  Math.atan((10 ** 3 * Math.sqrt(a)) / (2 * x - b))

puts "Result:"
puts L
