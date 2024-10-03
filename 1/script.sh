$l=100
for i in $(seq 1 5);
do
	./main $l 1
	l=$(($l * 10))
	echo $l
done
