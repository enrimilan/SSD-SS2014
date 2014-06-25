declare namespace saxon = "http://saxon.sf.net/";
declare option saxon:output "indent=yes";
<stats>
{
for $i  in data(/quiz/users/user/@name)
let $j := count(/quiz/games/game/round/answers/player[@ref=$i]/answer)
order by $i
return element 
	{lower-case($i)} {
		attribute sumTime { sum(/quiz/games/game/round/answers/player[@ref=$i]/answer/@time)},
		if($j> 0) then attribute avgTime { avg(/quiz/games/game/round/answers/player[@ref=$i]/answer/@time)}
		else attribute avgTime {0},
		if($j> 0) then <answerCount>{$j}</answerCount>
		else  <message>No answers for this user found</message>
		}
}
</stats>
