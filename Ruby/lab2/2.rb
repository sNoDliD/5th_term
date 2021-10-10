$, = " "
$\ = "\n"

def task1(ver)
  sum = 0
  (0...ver.size).each { |i|
    xk, yk = ver[-ver.size + i]
    xk1, yk1 = ver[-ver.size + i + 1]
    sum += (xk + xk1) * (yk - yk1)
  }
  sum.abs.fdiv 2
end

ver = [
  [34, 145], [37, 105], [41, 66], [70, 56], [99, 49],
  [134, 41], [152, 37], [185, 35], [226, 31], [282, 30],
  [334, 31], [413, 44], [450, 63], [473, 107], [490, 138],
  [500, 217], [495, 255], [470, 278], [445, 278], [401, 255],
  [396, 216], [396, 175], [381, 139], [381, 118], [310, 117],
  [289, 130], [256, 120], [240, 99], [219, 84], [172, 82],
  [152, 90], [139, 100], [113, 116], [97, 126], [68, 143]
]
# ver = [[1, 2], [4,2], [5,3]]
print "task1:", task1(ver)

def task2(p, t, r)
  (p ** r * (1 - p ** (-t))).ceil
end

# print "task2:", task2(6, 15, 10)
print "task2:", task2(5, 8, 0)

def task3(s_digit)
  e = 1
  res = 0
  s_digit.reverse.each_char do |x|
    res += x.to_i * e
    e *= 2
  end
  res
end

# print "task3:", task3("1001")
print "task3:", task3("1000101001001")

def task4(i_digit)
  return "0" if i_digit == 0
  res = ""
  while i_digit != 0 do
    i_digit, b = i_digit.divmod 2
    res.insert 0, b.to_s
  end
  res
end

# print "task4:", task4(11)
print "task4:", task4(192)
