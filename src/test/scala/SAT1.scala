import DPLL.{initWatchers, solve}
import cats.effect.{ExitCode, IO, IOApp}
import org.scalactic.TypeCheckedTripleEquals._

object SAT1 extends IOApp {
  val ax1 = Atom(Var("1"))
  val ax2 = Atom(Var("2"))
  val ax3 = Atom(Var("3"))
  val ax4 = Atom(Var("4"))


  val x1: Literal[Theory] = Atom(Var("1"))
  val x2: Literal[Theory] = Atom(Var("2"))
  val x3: Literal[Theory] = Atom(Var("3"))
  val x4: Literal[Theory] = Atom(Var("4"))

  val c1                                        = x1 || x2 || !x3 || x4
  val c2                                        = !x1 || !x3
  val c3                                        = !x2 || x4
  val c4                                        = !x4 || !x2
  val conj: Conjunct[Disjunct[Literal[Theory]]] = Conjunct(Seq(c1, c2, c3, c4))
  val activeRingStart = ActiveRing(Vector(ax1, ax2, ax3, ax4))

  override def run(args: List[String]): IO[ExitCode] = {
    val dpllStart = DPLL(CNForm(conj), Trace(), Watchers(initWatchers(conj)), activeRingStart)

    for {
      agg <- solve(dpllStart, Vector.empty, 0, Vector.empty, isStart = true)
      isTestPassed <- IO.pure(agg.nonEmpty)
      _ <- IO.pure {
        println(s"Test passed = $isTestPassed")
      }
    } yield ExitCode.Success
  }

}
