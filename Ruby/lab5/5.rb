def f1(x)
  (2 ** x - 1) ** (1 / 2)
end

def f2(x)
  1.fdiv(1 + (2 * x) ** (1 / 2))
end

def prm(left, right, func, eps=1e5)
  h = (right - left) / eps
  sum = 0
  (1..eps).each do |i|
    x = left + i * h - h / 2
    sum += func.call(x)
  end
  h * sum
end

def trp(left, right, func, eps=1e5)
  h = (right - left) / eps
  sum_a = func.call(left) / 2
  sum_b = func.call(right) / 2
  (1..(eps / 2)).each do |i|
    sum_a += func.call(left + h * i)
    sum_b += func.call(right - h * i)
  end
  h * (sum_a + sum_b)
end

puts 'prm for f1: ', prm(0.2, 1, method(:f1))
puts 'trp for f1: ', trp(0.2, 1, method(:f1))
puts 'prm for f2: ', prm(0.2, 1, method(:f2))
puts 'trp for f2: ', trp(0.2, 1, method(:f2))


def f(x, i)
  x**i * Math.cos(i*Math::PI/3)
end

def range(x, n)
  sum = 0
  if n.zero?
    prev = 1
    i = 0
    while prev > 0.001
      prev = f(x, i)
      sum += prev
      i += 1
    end
  else
    (0..n).each do |i|
      sum += f(x, i)
    end
  end
  sum
end

puts 'Input n (from 10 to 58 or 0 to count infinitely): '
n = gets.chomp.to_i
if n != 0
  while (n < 18) || (n > 58)
    puts 'Wrong number'
    n = gets.chomp.to_i
  end
end

x = 0.1
while x <= 0.8
  print "x=", x.round(2), " S=", range(x, n), "\n"
  x += 0.1
end
