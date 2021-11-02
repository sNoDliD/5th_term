class House
  attr_accessor :id, :number, :area, :floor, :room_count, :street, :build_type, :service_life

  def initialize(id, number, area, floor, room_count, street, build_type = "bricks", service_life = 6)
    @id = id
    @number = number
    @area = area
    @floor = floor
    @room_count = room_count
    @street = street
    @build_type = build_type
    @service_life = service_life
  end

  def self.get_houses
    [
      House.new(1, 23, 43, 3, 2, "Sechenova"),
      House.new(2, 24, 55, 9, 3, "Sechenova"),
      House.new(3, 24, 30, 9, 1, "Kraschatic"),
      House.new(4, 33, 25, 10, 1, "Goloseevo"),
      House.new(5, 666, 30, 9, 2, "GreatVasilkibska"),
    ]
  end

  def self.select_room_count(houses, room_count)
    houses.select { |house| house.room_count == room_count}
  end

  def self.select_room_count_with_floor(houses, room_count, floor_min, floor_max)
    houses.select { |house| house.room_count == room_count and house.floor >= floor_min and house.floor <= floor_max}
  end

  def self.select_large_area(houses, area)
    houses.select { |house| house.area > area}
  end

  def to_s
    "#{self.street} #{self.number}, #{self.room_count} (#{self.floor} #{self.area})"
  end
end


houses = House.get_houses

puts "2 rooms: ", House.select_room_count(houses, 2), "\n"
puts "2 rooms and floor in [8, 12]: ", House.select_room_count_with_floor(houses, 2, 8, 12), "\n"
puts "with area more than 25: ", House.select_large_area(houses, 25)
