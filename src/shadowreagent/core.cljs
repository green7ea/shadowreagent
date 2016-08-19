(ns shadowreagent.core
    (:require [reagent.core :as reagent :refer [atom]]))

(def starting-karma 600)
(def karma (reagent/atom 600))

(def stats {:for "Force"
            :agi "Agilité"
            :per "Perception"
            :cha "Charisme"
            :men "Mental"
            :int "Intuition"
            :edge "Chance"
            :ess "Essence"})

(def metatypes
     {:humain [0 "Humain (0 karma)" {} {}]
      :elfe [90 "Elfe (90 karma)" {:agi 1 :per 1 :cha 1} {}]
      :nain [50 "Nain (50 karma)" {:for 2 :men 1} {:per 5}]
      :ork [50 "Ork (50 karma)" {:for 3} {:cha 5}]
      :troll [70 "Troll (70 karma)" {:for 4} {:cha 4 :per 5}]})

(def sexe
     {:male [0 "Male"]
      :femelle [0 "Femelle"]})

(def character {:name (reagent/atom "")
                :metatype (reagent/atom :humain)
                :age (reagent/atom 24)
                :sexe (reagent/atom :male)
                :traits (reagent/atom [])
                :attribs (reagent/atom {})
                :competences (reagent/atom {})
                :ressources (reagent/atom [])})

(def bonus [["Affinité avec les esprits" "+1 service, ctrl +1" 7]
            ["Ambidextre" "Pas - 2 mauvaise main" 4]
            ["Apparence humaine " "Passer pour un humain" 6]
            ["Aptitude" "Max. Compétence +1" 14]
            ["Athlète né" "Course & Gymnast. +2" 7]
            ["Attribut exceptionnel" "Max. Attribut +1" 14]
            ["Bilingue" "2 langues M" 5]
            ["Bon codeur" "1 ac° matricielle +2" 10]
            ["Bricoleur" "Mécanique +2" 10]
            ["Chanceux" "Max. Chance +1" 12]
            ["Concentration accrue" "Maintien sort /FC N" 4]
            ["Contorsionniste" "Évasion +2" 6]
            ["Dur à cuire" "Résist. Dommages +1" 9]
            ["Endurance à la douleur" "Décalage N malus" 7]
            ["Esprit analytique" "Tests Logique +2" 5]
            ["Esprit mentor" "Cf. p.76" 5]
            ["Félin" "Discrétion +2" 7]
            ["Fou du volant" "+2 quand difficile" 11]
            ["Guérison rapide" "Guérison +2" 3]
            ["Immunité naturelle" 4]
            ["Immnuité synthétique" 10]
            ["M. Tout le monde" "Difficile à identifier" 8]
            ["Mémoire photographiq." "Mémoire +2" 6]
            ["Première impression" "1er test social +2" 11]
            ["Rage de vivre" "+1 case de surplus" 3]
            ["Renfort naturel" "Biofeedback +1" 10]
            ["Résistance à la magie " "Résister sorts +N" 6]
            ["Rés. pathogènes" 4]
            ["Rés. toxines" 4]
            ["Territoire" "Cf. p.78" 10]
            ["Tripes" "Rés. peur, intimid° +2" 10]])

(def malus [["Addiction" "Cf. p. 79" 25]
            ["Allergie" "Cf. p.79" 25]
            ["Asocial" "Test social - 2, etc." 14]
            ["Balise astrale " "Seuil sign. -1" 10]
            ["Code d’honneur" "Cf. p.80" 15]
            ["Crise de confiance" "Compétence 4+, -2" 10]
            ["Écorché" "RV: test ou pb" 10]
            ["Gremlins" "Seuil complica° objet -N" 4]
            ["Hostilité des esprits " "M Invoquer / lier - 2" 7]
            ["Caméléon astral " "M - 2 voir sign. astrale" 10]
            ["Illettré" "Ignorant comp. tech." 8]
            ["Immunodéficience" "Puissance maladie +2" 10]
            ["Incompétent" "Ignorant grp comp." 5]
            ["Insomnie légère" "Cf. p.84" 10]
            ["Insomnie sévère" "Cf. p.84" 15]
            ["Mal du simsens" "RA, RV, simsens: - 2" 5]
            ["Malchance" "1 sur 1d6: CHA inverse" 12]
            ["Mauvais codeur" "1 ac° matricielle - 2" 10]
            ["Mauvaise réputation" "Rumeur 3" 7]
            ["Paralysie au combat" "1er init ÷2, Surprise -3" 12]
            ["Personnes à charge" "Augmenta° style de vie" 9]
            ["Poseur elfe" "Cf. p.85" 6]
            ["Poseur ork" "Cf. p.85" 6]
            ["Préjugés raciste" 10]
            ["Sensibilité à la douleur" "-1 / 2 cases" 9]
            ["SIN national" "Cf. p.86" 5]
            ["SIN criminel" "Cf. p.86" 10]
            ["SIN corpo limité" "Cf. p.87" 15]
            ["SIN natif corpo" "Cf. p.87" 25]
            ["Style distinctif" "Vu: +2 et seuil -1" 5]
            ["Système sensible" "Cyber x2, Drain +2" 12]
            ["Traumatisme" "Seuil complica° soc. -N" 8]
            ["Tremblement mains" "Test ou AGI -2" 7]])

(def competances
     {:combat {:exotiques [:agi "Armes exotiques"]
               :jet [:agi "Armes de jet"]
               :esquive [:agi "Esquive"]
               :melee [:any "Mêlée"]
               :epaule [:per "Fusil d'épaule"]
               :main [:per "Fusil à main"]
               :trait [:per "Armes de trait"]
               :artillerie [:per "Artillerie"]}
      :physique {:athletisme [:any "Athlétisme"]
                 :survie [:men "Survie"]
                 :manuelle [:agi "Habileté manuelle"]
                 :infiltration [:agi "Infiltration"]}
      :sociale {:etiquette [:cha "Étiquette"]
                :influence [:cha "Influence"]
                :performance [:cha "Performance"]}
      :magique {:conjuration [:men "Conjuration"]
                :enchantement [:men "Enchantement"]
                :rituelle [:men "Magie rituelle"]
                :astrale [:men "Observation astrale"]
                :resonance [:men "Résonance"]
                :sorcellerie [:men "Sorcellerie"]}
      :technique {:animaux [:int "Animaux"]
                  :artisanat [:int "Artisanat"]
                  :explosifs [:int "Explosifs"]
                  :chimie [:men "Chimie"]
                  :cybertech [:men "Cybertechnologie"]
                  :biotech [:men "Biotechnologie"]
                  :informatique [:men "Informatique"]
                  :logiciel [:men "Logiciel"]
                  :electronique [:men "Matériel électronique"]
                  :mecanique [:men "Mécanique"]
                  :piratage [:men "Piratage"]}
      :pilotage {:aerien [:per "Véhicules aériens"]
                 :exotique [:per "Véhicules exotiques"]
                 :maritime [:per "Véhicules maritimes"]
                 :terrestre [:per "Véhicules terrestres"]}})


(defn get-bonus [attr-key]
  (or (((metatypes @(character :metatype)) 2) attr-key) 0))

(defn get-max [attr-key]
  (+ (or (((metatypes @(character :metatype)) 3) attr-key) 6)
     (get-bonus attr-key)))

(defn value->karma [value min]
  (loop [count value
         karma 0]
        (if (<= count min)
            karma
            (recur (- count 1)
                   (+ (* count count)
                      karma)))))

(defn calculate-attrib-karma []
  (reduce +
          (map (fn [[key value]]
                   (value->karma (- value
                                    (get-bonus key))
                                 1))
               @(character :attribs))))

(defn calculate-comp-karma [test-key]
  (reduce +
          (map (fn [[key value]]
                   (if (or (nil? test-key)
                           ((competances test-key) key))
                       (value->karma value 0)
                       0))
               @(character :competences))))

(defn calculate-metatype-karma []
  ((metatypes @(character :metatype)) 0))

(defn calculate-karma []
  (+ (calculate-metatype-karma)
     (calculate-attrib-karma)
     (calculate-comp-karma nil)))

(defn update-karma! []
  (swap! karma #(- starting-karma
                   (calculate-karma))))

(defn create-option [var list]
  [:select {:class "ui dropdown right"
            :on-change #(do (reset! var (keyword (-> % .-target .-value)))
                            (update-karma!))}
           (for [item list]
                [:option {:key (first item) :value (first item)} ((second item) 1)])])

(defn create-field [name var]
  [:div {:class "ui input focus"}
        [:input {:type "text"
                 :value @var
                 :on-change #(reset! var (-> % .-target .-value))}]])

(defn rating-trigger [obj]
  (do
      (let [msg (-> obj .-target .-id)]
        (let [key (keyword (re-find #"[a-z]+" msg))
              value (re-find #"[0-9]+" msg)]
          (if (stats key)
              (reset! (character :attribs) (assoc @(character :attribs) key value))
              (reset! (character :competences) (assoc @(character :competences) key value)))))
      (update-karma!)))

(defn build-rating [key min max]
  (into [] (concat
            [:div.stars]
            (interleave
             (map (fn [i] [:input {:class (str "star star-" i)
                                   :id (str key "-" i)
                                   :type "radio"
                                   :name key
                                   :key key
                                   :on-change rating-trigger}])
                  (range max min -1))
             (map (fn [i] [:label {:class (str "star star-" i)
                                          :for (str key "-" i)}])
                  (range max min -1)))
            (map (fn [i]
                     [:label.fixed])
                 (range min)))))

(defn basic-card []
  [:div.ui.card
   [:div.header [:h1 "Base"]]
   [:div.meta.no-print (str "Karma: "
                            (calculate-metatype-karma))]
   [:div.content
    [:table.ui.celled.table
     [:tbody
      [:tr [:td "Nom " (create-field "Nom" (character :name))]]
      [:tr [:td "Métatype " (create-option (character :metatype) metatypes)]]
      [:tr [:td "Âge " (create-field "Age" (character :age))]]
      [:tr [:td "Sexe " (create-option (character :sexe) sexe)]]]]]])

(defn attribute-card []
  [:div.ui.card
   [:div.header [:h1 "Attributs"]]
   [:div.meta.no-print (str "Karma: "
                            (calculate-attrib-karma)
                            " / 170")]
   [:div.content
    [:table.ui.celled.table
     (into [] (concat
               [:tbody]
               (map (fn [[key text]]
                        [:tr {:key key}
                             [:td text]
                             [:td
                              (build-rating key
                                            (get-bonus key)
                                            (get-max key))]])
                    stats)))]]])

(defn comp-card [[key data]]
  [:div.ui.card {:key key}
                [:div.header [:h1 key]]
                [:div.meta.no-print (str "Karma: "
                                         (calculate-comp-karma key))]
                [:div.content
                 [:table.ui.celled.table
                  [:tbody
                   (map (fn [[sub-key [attr text]]]
                            [:tr {:key (str key sub-key)}
                                 [:td text]
                                 [:td (stats attr)]
                                 [:td (build-rating sub-key 0 10)]])
                        data)]]]])

(defn comp-cards []
  [:div.ui.cards
   (map comp-card competances)])

(defn home-page []
  [:section.ui
   [:div.ui.enormous.image
    [:img {:src "logo.png"}]]
   [:div.karma.no-print
    [:h2 "Karma " @karma]]

   [:div.ui.cards
    (basic-card)
    (attribute-card)]

   (comp-cards)])

(defn mount-root []
  (reagent/render [home-page]
                  (.getElementById js/document "app")))

(defn init! []
  (mount-root))
