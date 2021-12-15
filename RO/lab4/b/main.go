package main

import (
	"fmt"
	"math/rand"
	"os"
	"strconv"
	"sync"
	"time"
)

func gardener(garden [][]int, m *sync.RWMutex) {
	time.Sleep(1500 * time.Millisecond)
	for {
		m.Lock()
		for i := 0; i < 5; i++ {
			for j := 0; j < 5; j++ {
				if garden[i][j] == 0 {
					garden[i][j] = 1
				}
			}
		}
		m.Unlock()
		time.Sleep(2000 * time.Millisecond)
	}
}

func nature(garden [][]int, m *sync.RWMutex) {
	rand.Seed(time.Now().UTC().UnixNano())
	time.Sleep(500 * time.Millisecond)
	for {
		m.Lock()
		for i := 0; i < 5; i++ {
			for j := 0; j < 5; j++ {
				garden[i][j] = rand.Intn(2);
			}
		}
		m.Unlock()
		time.Sleep(2000 * time.Millisecond)
	}
}

func monitor1(garden [][]int, m *sync.RWMutex) {
	file, err := os.Create("output.txt")

	if err != nil {
		fmt.Println("Unable to open file:", err)
		os.Exit(1)
	}
	defer file.Close()

	for {
		m.RLock()
		for i := 0; i < 5; i++ {
			for j := 0; j < 5; j++ {
				file.WriteString(strconv.Itoa(garden[i][j]))
			}
			file.WriteString("\n")
		}
		file.WriteString("\n\n\n")
		m.RUnlock()
		time.Sleep(1000 * time.Millisecond)
	}
}

func monitor2(garden [][]int, m *sync.RWMutex) {
	for {
		m.RLock()
		for i := 0; i < 5; i++ {
			for j := 0; j < 5; j++ {
				fmt.Print(garden[i][j])
			}
			fmt.Println()
		}
		fmt.Println()
		m.RUnlock()
		time.Sleep(1000 * time.Millisecond)
	}
}

func main() {
	var garden [][]int
	var wg sync.WaitGroup
	var m sync.RWMutex

	for j := 0; j < 5; j++ {
		var row []int
		for i := 0; i < 5; i++ {
			row = append(row, 0)
		}
		garden = append(garden, row)
	}

	wg.Add(4)

	go monitor1(garden, &m)
	go monitor2(garden, &m)
	go nature(garden, &m)
	go gardener(garden, &m)

	wg.Wait()
}
