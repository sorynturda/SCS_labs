#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <time.h>

#define	rdtsc(low, high)	__asm__ volatile ("rdtsc;" : "=a" (low), "=d" (high))
#define	__rdtsc__()		__asm__ volatile ("rdtsc;" ::: "eax", "edx")
#define	cpuid()			__asm__ volatile ("cpuid;" :: "a"(0) : "bx", "cx", "dx")

#define	pushad()		__asm__ volatile ("pushal;")
#define	popad()			__asm__ volatile ("popal;")
#define	mov(dst, src)		__asm__ volatile ("mov %1, %0;" : "=r"(dst) : "r"(src))
#define	add(dst, src)		__asm__ volatile ("add %1, %0;" : "=r"(dst) : "r"(src), "0"(dst))
#define	sub(dst, src)		__asm__ volatile ("sub %1, %0;" : "=r"(dst) : "r"(src), "0"(dst))
#define	sub_reg(reg, src)	__asm__ volatile ("sub %0, %%"reg :: "m"(src))
#define	mov_reg(dst, reg)	__asm__ volatile ("mov %%"reg", %0" : "=m"(dst))

#define FREQUENCY 28000000
#define RUNS 10

// define assemby macros for operations here
// you can use the "Extended Asm" documentation for GNU GCC 
// https://gcc.gnu.org/onlinedocs/gcc/Extended-Asm.html

int partition(uint32_t *, int, int);

void insertion_sort(uint32_t *a, int n) {
    for (int i = 1; i < n; i++) {
        int x = a[i];
        int j = i - 1;
        while (j >= 0 && a[j] > x) {
            a[j + 1] = a[j];
            j--;
        }
        a[j + 1] = x;
    }
}

void quicksort_r(uint32_t *a, int left, int right) {
    if (left >= right)
        return;
	int i = rand() % (right - left + 1) + left;
	int tmp = a[i];
	a[i] = a[right];
	a[right] = tmp;	
    int q = partition(a, left, right);
    quicksort_r(a, left, q - 1);
    quicksort_r(a, q + 1, right);
}

int partition(uint32_t *a, int left, int right) {
    int x = a[right]; // pivot elementul cel mai din dreapta
    int i = left - 1;
    for (int j = left; j <= right - 1; j++) {
        if (a[j] <= x) {
            i++;
			int tmp = a[i];
			a[i] = a[j];
			a[j] = tmp;
        }
    }
	int tmp = a[i+1];
	a[i+1] = a[right];
	a[right] = tmp;
    return i + 1; // pozitia unde e amplasat pivotul
}

void sort(uint32_t *array, int len)
{
	insertion_sort(array, len);
	// implement sorting strategy here
}

void optimized_sort(uint32_t *array, int len)
{
	// implement the optimized version of the sorting strategy here
	quicksort_r(array, 0, len-1);
}

void print_array(uint32_t *array, int len)
{
	for (int i=0; i<len; ++i)
		printf("%u ", array[i]);
	printf("\n");
}

// !do not forget to use this function to generate the arrays
void generate_random_array(uint32_t *array, int len)
{
	srand(time(NULL));
	for (int i=0; i<len; ++i)
		array[i] = (uint32_t)(rand() % 1000);
}

int main(void) 
{
	uint32_t cycles_high1 = 0, cycles_low1 = 0, cpuid_time = 0;
	uint32_t cycles_high2 = 0, cycles_low2 = 0;
	uint64_t temp_cycles1 = 0, temp_cycles2 = 0;
	int64_t total_cycles = 0;
	double avg_cycles = 0.0f, avg_seconds = 0.0f, total_seconds = 0.0f; 
	double avg_clock_seconds = 0.0f;
	// declare necessary variables here
	uint32_t array1[10000]; // static array definition
	uint32_t *array2 = (uint32_t *)malloc(10000 * sizeof(uint32_t)); // dynamic array definition
	for (int run = 0; run < RUNS; ++run) {
		generate_random_array(array1, 10000);
		// compute the CPUID overhead
		pushad();
		cpuid();
		__rdtsc__();
		popad();
		pushad();
		cpuid();
		__rdtsc__();
		popad();
		pushad();
		cpuid();
		__rdtsc__();
		popad();
		pushad();
		cpuid();
		__rdtsc__();
		popad();
	
		pushad();
		cpuid();
		rdtsc(cycles_low1, cycles_high1);
		popad();
		pushad();
		cpuid();
		__rdtsc__();
		sub_reg("eax", cycles_low1);
		mov_reg(cpuid_time, "eax");
		popad();
	
		cycles_high1 = cycles_low1 = 0;
	
		// measure start timestamp
		pushad();
		cpuid();
		rdtsc(cycles_low1, cycles_high1);
		popad();
	
		// section of code to be measured
		// after measuting the execution time for both array1 and array2 using the sort function
		// do the same for the optimized sort 
		double begin = clock();

		// optimized_sort(array1, 10000);
		sort(array1, 10000);
		
		double end = clock();
		avg_clock_seconds = (double)(end - begin);
		// measure stop timestamp
		pushad();
		cpuid();
		rdtsc(cycles_low2, cycles_high2);
		popad();
	
		// compute 64bit value of the passed time
		temp_cycles1 = ((uint64_t)cycles_high1 << 32) | cycles_low1;
		temp_cycles2 = ((uint64_t)cycles_high2 << 32) | cycles_low2;
		total_cycles = temp_cycles2 - temp_cycles1 - cpuid_time;
		avg_cycles += total_cycles;
	}

	avg_cycles /= (double)RUNS;
	avg_seconds = avg_cycles / (double)FREQUENCY;
	total_seconds = (double) total_cycles / (double)FREQUENCY;
	avg_clock_seconds /= (double)RUNS;
	
	printf("Average cycles = %lf\n", avg_cycles);
	printf("Average seconds = %lf\n", avg_seconds);
	printf("Cycles (last run) = %lld\n", total_cycles);
	printf("Seconds (last run) = %lf\n", total_seconds);
	printf("Average clock() cycles  = %lf\n", avg_clock_seconds);
	return 0;
}


