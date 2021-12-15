package main

import (
	"fmt"
	"math/rand"
)

func sum(Array [] int) int {
	Sum := 0
	for _, v := range Array {
		Sum += v
	}
	return Sum
}

func process(Name string, Array [] int,  StartTrigger chan int, DoneTrigger chan int, LastAvg * int) {

	for {
		<- StartTrigger

		fmt.Println("Array", Name, "before:", Array)

		pos := rand.Intn(len(Array))
		Sum := sum(Array)
		if  Sum < *LastAvg {
			Array[pos]++
		} else if Sum > *LastAvg {
			Array[pos]--
		} else {
			Array[pos] += rand.Intn(3) - 1
		}

		fmt.Println("Array", Name, "after:", Array)

		DoneTrigger <- sum(Array)
	}

}

func main() {

	LastAvg := 0

	ArrayA := [] int {123, 432, 543, 123}
	StartTriggerA := make(chan int)
	DoneTriggerA := make(chan int)
	go process("A", ArrayA, StartTriggerA, DoneTriggerA, &LastAvg)

	ArrayB := [] int {432, 432, 643, 345}
	StartTriggerB := make(chan int)
	DoneTriggerB := make(chan int)
	go process("B", ArrayB, StartTriggerB, DoneTriggerB, &LastAvg)

	ArrayC := [] int {432, 556, 753, 768}
	StartTriggerC := make(chan int)
	DoneTriggerC := make(chan int)
	go process("C", ArrayC, StartTriggerC, DoneTriggerC, &LastAvg)

	for {
		StartTriggerA <- 1
		StartTriggerB <- 1
		StartTriggerC <- 1

		sumA := <- DoneTriggerA
		sumB := <- DoneTriggerB
		sumC := <- DoneTriggerC

		fmt.Println("Sums:", sumA, ",", sumB, ",", sumC)
		fmt.Println()

		LastAvg = (sumA + sumB + sumC) / 3

		if sumA == sumB && sumA == sumC {
			return
		}
	}
}