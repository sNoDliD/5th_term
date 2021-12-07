require 'date'

class Mail
  attr_accessor :date_get, :date_read, :title, :tag

  def initialize(date_get, date_read, title, tag)
    @title = title
    @tag = tag
    @date_get = date_get
    @date_read = date_read
  end

  def self.filter_by_tag(mails, tag)
    mails.select { |mail| mail.tag == tag }
  end

  def self.filter_by_title(mails, title)
    mails.select { |mail| mail.title == title }
  end

  def self.filter_by_date_get(mails, date_start, date_end)
    mails.select { |mail| mail.date_get <= date_end and date_start <= mail.date_get }
  end
end

class Sending < Mail
  attr_accessor :delivery_time

  def initialize(date_get, date_read, title, tag, delivery_time)
    super(date_get, date_read, title, tag)
    @delivery_time = delivery_time
  end

end

class Letter < Mail
  attr_accessor :genre

  def initialize(date_get, date_read, title, tag, genre)
    super(date_get, date_read, title, tag)
    @genre = genre
  end
end

class Parcel < Mail
  attr_accessor :weight

  def initialize(date_get, date_read, title, tag, weight)
    super(date_get, date_read, title, tag)
    @weight = weight
  end
end

all_mails = [
  Sending.new(Date.new(2021, 12, 7), nil, "Exam", "Important", 5),
  Sending.new(Date.new(2021, 12, 8), Date.new(2021, 12, 8), "Exam", "Spam", 15),
  Letter.new(Date.new(2021, 12, 8), Date.new(2021, 12, 8), "OOP", "Spam", "Horror"),
  Letter.new(Date.new(2021, 12, 8), Date.new(2021, 12, 8), "Exam", "Other", "Fantasy"),
  Parcel.new(Date.new(2021, 12, 9), nil, "Exam", "Spam", 55),
  Parcel.new(Date.new(2021, 12, 10), Date.new(2021, 12, 10), "Ruby", "Spam", 1035),
]

p Mail.filter_by_tag(all_mails, 'Spam')
p Mail.filter_by_title(all_mails, 'Exam')
p Mail.filter_by_date_get(all_mails, Date.new(2021, 12, 8), Date.new(2021, 12, 9))
