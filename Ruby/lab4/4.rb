require 'matrix'

a = Array.new(24)

i = 0
while i < 24
  a[i] = rand(-10..10)
  i += 1
end

sum_even = 0
sum_odd = 0

a.each do |x|
  sum_even += x if x.even? && x.negative?
  sum_odd += x if x.odd? && x.positive?
end
print "0.6\n"
print 'a: ', a, "\n"
print 'sum even: ', sum_even, "\n"
print 'sum odd: ', sum_odd, "\n"
print 'total sum: ', sum_even + sum_odd, "\n\n"

def matrix_print(matrix)
  matrix_ = Matrix[*matrix]
  puts matrix_.to_a.map(&:inspect)
end

temp_a = (0...3).map { Array.new 3 }
temp_b = (0...3).map { Array.new 3 }

(0...3).each do |i|
  (0...3).each do |j|
    temp_a[i][j] = rand(10)
    temp_b[i][j] = rand(10)
    temp_a[i][j] = 1 if i == j
  end
end

a = Matrix[*temp_a]
b = Matrix[*temp_b]
x = Vector[*Array.new(3) { rand(10) }]
y = Vector[*Array.new(3) { rand(10) }]

def task1(matrix, number)
  temp = Array.new(matrix.size) do |i|
    Array.new(matrix[0].size) do |j|
      matrix[i][j] * number
    end
  end
  Matrix[*temp]
end

def task2(matrix1, matrix2)
  temp = Array.new(matrix1.size) do |i|
    Array.new(matrix2[0].size) do |j|
      matrix1[i][j] + matrix2[i][j]
    end
  end
  Matrix[*temp]
end

def task3(matrix)
  temp = Array.new(matrix[0].size) do |i|
    Array.new(matrix.size) do |j|
      matrix[j][i]
    end
  end
  Matrix[*temp]
end

def task4(matrix1, matrix2)
  temp = Array.new(matrix1.size) do |i|
    Array.new(matrix2[0].size) do |j|
      matrix2.size.times.inject(0) { |result, k| result + matrix1[i][k] * matrix2[k][j] }
    end
  end
  Matrix[*temp]
end

def task5(matrix)
  matrix.size.times.inject(0) { |result, i| result + matrix[i][i] }
end

def task6(vector1, vector2)
  temp = Array.new(vector1.size) do |i|
    vector1[i] * vector2[i]
  end
  Vector[*temp]
end

matrix1 = task1(b.to_a, 2)
matrix2 = task2(a.to_a, b.to_a)
matrix3 = task3(a.to_a)
matrix4 = task4(a.to_a, b.to_a)
trace5 = task5(b.to_a)
vector6 = task6(x.to_a, y.to_a)

print "1\n"
puts 'A: '
matrix_print(a)
puts 'B: '
matrix_print(b)
puts 'X: ', x
puts 'Y: ', y
puts 'task1: '
matrix_print(matrix1)
puts 'task2: '
matrix_print(matrix2)
puts 'task3: '
matrix_print(matrix3)
puts 'task4: '
matrix_print(matrix4)
puts 'task5: ', trace5
puts 'task6: ', vector6


print "2\n"
puts 'Input dimension (from 3 to 9): '
n = gets.chomp.to_i
while (n < 3) || (n > 9)
  puts 'Wrong number'
  n = gets.chomp.to_i
end

a = (0..(n - 1)).map { Array.new n }
b = Array.new n
x = Array.new n

(0..(n - 1)).each do |i|
  (0..(n - 1)).each do |j|
    a[i][j] = 14.0
    a[i][j] = 2.0 if i == j
  end
  b[i] = i + 1.0
end

puts "a:"
matrix_print a
print "b: ", b, "\n"

(1..(n - 1)).each do |k|
  (k..(n - 1)).each do |i|
    coefficient = a[i][k - 1] / a[k - 1][k - 1]
    ((k - 1)..(n - 1)).each do |j|
      a[i][j] -= coefficient * a[k - 1][j]
    end
    b[i] -= coefficient * b[k - 1]
  end
end

x[n - 1] = b[n - 1] / a[n - 1][n - 1]
i = n - 2
while i >= 0
  ((i + 1)..(n - 1)).each do |j|
    b[i] -= x[j] * a[i][j]
  end
  x[i] = b[i] / a[i][i]
  i -= 1
end
puts 'result: '
p x